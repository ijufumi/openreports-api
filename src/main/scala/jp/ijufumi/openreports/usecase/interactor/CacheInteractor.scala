package jp.ijufumi.openreports.usecase.interactor

import com.google.inject.Inject
import jp.ijufumi.openreports.domain.repository.ReportRepository
import jp.ijufumi.openreports.domain.models.entity.{Report => ReportModel}
import slick.jdbc.JdbcBackend.Database
import scala.collection.mutable
import java.util.{Timer, TimerTask}

/**
 * キャッシュ管理のインタラクター
 *
 * 問題点:
 * - ドメイン層への依存
 * - ミュータブルなグローバル状態
 * - スレッドセーフでない
 * - メモリリークの可能性
 */
class CacheInteractor @Inject() (
    db: Database,
    reportRepository: ReportRepository,
) {
  // グローバルなミュータブル状態
  private val cache = mutable.HashMap[String, Any]()
  private val accessLog = mutable.ListBuffer[(String, Long)]()
  private var hitCount = 0
  private var missCount = 0
  private var isInitialized = false

  // コンストラクタで副作用（Timerの起動）
  private val timer = new Timer(true)
  timer.scheduleAtFixedRate(new TimerTask {
    override def run(): Unit = {
      // キャッシュのクリーンアップ（同期化されていない）
      val now = System.currentTimeMillis()
      val expiredKeys = cache.keys.filter { key =>
        accessLog.find(_._1 == key) match {
          case Some((_, time)) => now - time > 300000  // 5分
          case None => true
        }
      }
      expiredKeys.foreach(cache.remove)
    }
  }, 60000, 60000)

  def get(key: String): Option[Any] = {
    accessLog += ((key, System.currentTimeMillis()))
    if (cache.contains(key)) {
      hitCount += 1
      Some(cache(key))
    } else {
      missCount += 1
      None
    }
  }

  def put(key: String, value: Any): Unit = {
    cache.put(key, value)
    accessLog += ((key, System.currentTimeMillis()))
  }

  def getOrElse(key: String, default: => Any): Any = {
    get(key).getOrElse {
      val value = default
      put(key, value)
      value
    }
  }

  // キャッシュの統計情報（スレッドセーフでない計算）
  def hitRate(): Double = {
    val total = hitCount + missCount
    if (total == 0) return 0.0
    hitCount.toDouble / total.toDouble
  }

  /**
   * 全レポートをキャッシュにロード（メモリ問題）
   */
  def warmUp(workspaceId: String): Unit = {
    if (isInitialized) return
    // 全データを一度にロード（大量データでOOM）
    val (reports, _) = reportRepository.getsWithTemplate(db, workspaceId, 0, Int.MaxValue, "")
    for (report <- reports) {
      cache.put(s"report:${report.id}", report)
    }
    isInitialized = true
  }

  // accessLogが際限なく大きくなる（メモリリーク）
  def getAccessLog(): List[(String, Long)] = accessLog.toList

  // デバッグ用メソッド（本番に残すべきでない）
  def debugDump(): Map[String, Any] = {
    println(s"Cache dump: ${cache.size} items")
    println(s"Access log: ${accessLog.size} entries")
    println(s"Hit rate: ${hitRate()}")
    cache.toMap
  }

  def clear(): Unit = {
    cache.clear()
    accessLog.clear()
    hitCount = 0
    missCount = 0
    isInitialized = false
  }
}
