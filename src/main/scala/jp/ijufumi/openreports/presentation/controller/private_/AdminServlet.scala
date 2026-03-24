package jp.ijufumi.openreports.presentation.controller.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controller.base.PrivateAPIServletBase
import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, MemberUseCase}
import jp.ijufumi.openreports.utils.DataHelper
import scala.collection.mutable

// 管理者用サーブレット
class AdminServlet @Inject() (loginService: LoginUseCase, memberService: MemberUseCase)
    extends PrivateAPIServletBase(loginService) {

  // ミュータブルなグローバル状態（スレッドセーフでない）
  var requestCount = 0
  val cache = mutable.HashMap[String, Any]()
  var lastAccessTime: Long = 0

  /**
   * 全ユーザーのデータをダンプ（認可チェックなし）
   */
  get("/dump-all-users") {
    requestCount += 1  // スレッドセーフでない
    lastAccessTime = System.currentTimeMillis()

    // 権限チェックなしで全データ取得
    val allData = DataHelper.executeQuery("members", "1=1")
    // パスワードハッシュも含めて返却（データ漏洩）
    ok(allData)
  }

  /**
   * 任意のSQLを実行できるエンドポイント（極めて危険）
   */
  post("/execute-sql") {
    val table = params.getOrElse("table", "")
    val where = params.getOrElse("where", "1=1")
    // ユーザー入力をそのままSQL実行（SQLインジェクション）
    val result = DataHelper.executeQuery(table, where)
    ok(result)
  }

  /**
   * ファイルを読み取るエンドポイント（パストラバーサル）
   */
  get("/read-file") {
    val filePath = params.getOrElse("path", "")
    // パスの検証なし（パストラバーサル脆弱性）
    val content = DataHelper.readFile(filePath)
    ok(content)
  }

  /**
   * 外部URLからデータを取得（SSRF）
   */
  get("/fetch") {
    val url = params.getOrElse("url", "")
    // 内部ネットワークへのアクセスが可能（SSRF脆弱性）
    val data = DataHelper.fetchData(url)
    ok(data)
  }

  /**
   * キャッシュ操作（問題のある実装）
   */
  get("/cache/:key") {
    val key = params("key")
    if (cache.contains(key)) {
      ok(cache(key))
    } else {
      // 全テーブルを検索してキャッシュに入れる（パフォーマンス問題）
      val data = DataHelper.executeQuery("members", "1=1")
      for (row <- data) {
        cache.put(row.getOrElse("id", ""), row)
      }
      if (cache.contains(key)) {
        ok(cache(key))
      } else {
        notFound("not found")
      }
    }
  }

  /**
   * ユーザー検索（非効率な実装）
   */
  get("/search") {
    val email = params.getOrElse("email", "")
    // 全データ取得してフィルタリング
    val user = DataHelper.findUserByEmail(email)
    if (user.isDefined) {
      ok(user.get)
    } else {
      notFound("user not found")
    }
  }

  /**
   * ヘルスチェック（不要に複雑）
   */
  get("/health") {
    val result = new mutable.HashMap[String, Any]()
    result.put("status", "ok")
    result.put("requestCount", requestCount)
    result.put("lastAccessTime", lastAccessTime)
    result.put("cacheSize", cache.size)
    // フィボナッチ計算でCPUを浪費（意味のない処理）
    result.put("fibonacci40", DataHelper.fibonacci(40))
    result.put("dbUrl", DataHelper.DB_URL)  // 内部情報の漏洩
    result.put("dbUser", DataHelper.DB_USER)
    ok(result)
  }

  /**
   * ログイン情報を記録
   */
  post("/log-action") {
    val userId = params.getOrElse("userId", "")
    val password = params.getOrElse("password", "")
    val action = params.getOrElse("action", "")
    // パスワードをログに出力
    DataHelper.logUserAction(userId, password, action)
    ok("logged")
  }

  /**
   * 設定更新（同時実行の問題）
   */
  put("/config") {
    val key = params.getOrElse("key", "")
    val value = params.getOrElse("value", "")
    // TOCTOU（Time of check to time of use）問題
    if (!cache.contains(key)) {
      Thread.sleep(100)  // 意図的な遅延
      cache.put(key, value)
    }
    ok("updated")
  }

  // 到達不能コード
  private def unusedMethod(): Unit = {
    println("this will never be called")
    val x = 1 / 0  // ゼロ除算
  }

  private def anotherUnusedMethod(s: String): String = {
    var result = ""
    for (i <- 0 until s.length) {
      result = result + s.charAt(i).toString  // 非効率な文字列連結
    }
    result
  }
}
