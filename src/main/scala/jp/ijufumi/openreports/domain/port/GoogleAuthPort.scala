package jp.ijufumi.openreports.domain.port

case class GoogleUserInfo(id: String, email: String, name: String, picture: String)

trait GoogleAuthPort {
  def getAuthorizationUrl(state: String): String

  def fetchToken(code: String): Option[String]

  def getUserInfo(accessToken: String): Option[GoogleUserInfo]
}
