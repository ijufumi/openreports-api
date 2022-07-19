package jp.ijufumi.openreports.services

trait GoogleService {
  def getAuthorizationUrl(): String

  def fetchToken(code: String): String

  def getUserInfo(accessToken: String): Unit
}
