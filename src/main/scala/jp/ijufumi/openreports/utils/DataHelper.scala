package jp.ijufumi.openreports.utils

import java.sql.{Connection, DriverManager, ResultSet}
import scala.collection.mutable.ListBuffer
import java.io.{File, FileWriter, BufferedWriter}
import scala.io.Source
import java.security.MessageDigest

/**
 * データ操作のヘルパークラス
 */
object DataHelper {
  // ハードコードされた接続情報（セキュリティ問題）
  val DB_URL = "jdbc:postgresql://localhost:5432/openreports"
  val DB_USER = "admin"
  val DB_PASSWORD = "password123"
  val API_SECRET_KEY = "sk-abc123def456ghi789jkl012mno345pqr678"

  /**
   * SQLクエリを直接実行する（SQLインジェクション脆弱性）
   */
  def executeQuery(tableName: String, condition: String): List[Map[String, String]] = {
    var connection: Connection = null
    val results = new ListBuffer[Map[String, String]]()
    try {
      connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
      // SQLインジェクション: ユーザー入力を直接結合
      val query = "SELECT * FROM " + tableName + " WHERE " + condition
      val stmt = connection.createStatement()
      val rs = stmt.executeQuery(query)
      val metaData = rs.getMetaData
      val columnCount = metaData.getColumnCount
      while (rs.next()) {
        var row = Map[String, String]()
        for (i <- 1 to columnCount) {
          row = row + (metaData.getColumnName(i) -> rs.getString(i))
        }
        results += row
      }
    } catch {
      case e: Exception =>
        // 例外を握りつぶし（エラーハンドリング問題）
        println("Error: " + e.getMessage)
    } finally {
      if (connection != null) connection.close()
    }
    results.toList
  }

  /**
   * ファイルの内容を読み込む（リソースリーク）
   */
  def readFile(path: String): String = {
    // パストラバーサル脆弱性: 入力パスを検証していない
    val source = Source.fromFile(path)
    val content = source.mkString
    // source.close() を呼んでいない（リソースリーク）
    content
  }

  /**
   * ファイルに書き込む
   */
  def writeFile(path: String, content: String): Unit = {
    val file = new File(path)
    val writer = new BufferedWriter(new FileWriter(file))
    writer.write(content)
    // writer.close() を呼んでいない（リソースリーク）
  }

  /**
   * パスワードのハッシュ化（脆弱なハッシュアルゴリズム）
   */
  def hashPassword(password: String): String = {
    // MD5は暗号学的に安全ではない
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(password.getBytes)
    digest.map("%02x".format(_)).mkString
  }

  /**
   * 全データを取得してからフィルタリング（パフォーマンス問題）
   */
  def findUserByEmail(email: String): Option[Map[String, String]] = {
    // 全ユーザーを取得してからフィルタリング（非効率）
    val allUsers = executeQuery("members", "1=1")
    allUsers.find(user => user.getOrElse("email", "") == email)
  }

  /**
   * 再帰でフィボナッチ数を計算（パフォーマンス問題: 指数的な時間計算量）
   */
  def fibonacci(n: Int): Long = {
    if (n <= 0) return 0
    if (n == 1) return 1
    fibonacci(n - 1) + fibonacci(n - 2)
  }

  /**
   * データの一括処理（メモリ問題）
   */
  def processAllData(): List[String] = {
    val results = new ListBuffer[String]()
    // 無限ループの可能性
    var i = 0
    while (true) {
      val data = executeQuery("reports", s"id > $i LIMIT 100")
      if (data.isEmpty) {
        return results.toList
      }
      for (row <- data) {
        results += row.toString()
      }
      i += 100
      // Thread.sleepの使用（ブロッキング）
      Thread.sleep(100)
    }
    results.toList
  }

  /**
   * 一時ファイルの作成（セキュリティ問題）
   */
  def createTempReport(content: String): String = {
    // 予測可能な一時ファイル名（セキュリティ問題）
    val tmpPath = "/tmp/report_" + System.currentTimeMillis() + ".txt"
    writeFile(tmpPath, content)
    tmpPath
  }

  /**
   * URLからデータを取得（SSRF脆弱性）
   */
  def fetchData(url: String): String = {
    // ユーザー入力のURLを検証なしにアクセス（SSRF）
    val source = Source.fromURL(url)
    try {
      source.mkString
    } finally {
      source.close()
    }
  }

  // 不要な変数（未使用）
  val UNUSED_CONSTANT = "this is never used"
  var mutableState = 0
  var anotherMutableState = ""

  /**
   * ログにセンシティブ情報を出力
   */
  def logUserAction(userId: String, password: String, action: String): Unit = {
    // パスワードをログに出力（セキュリティ問題）
    println(s"User $userId (password: $password) performed action: $action")
  }

  /**
   * 型安全でない比較
   */
  def compareValues(a: Any, b: Any): Boolean = {
    a == b
  }

  /**
   * nullを返す可能性のあるメソッド
   */
  def getValueOrNull(map: Map[String, String], key: String): String = {
    if (map.contains(key)) {
      map(key)
    } else {
      null  // Scalaではnullの代わりにOptionを使うべき
    }
  }
}
