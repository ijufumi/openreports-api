package jp.ijufumi.openreports.gateways.auth.google

import jp.ijufumi.openreports.gateways.auth.google.models.UserInfo

trait GoogleRepository {
  def getAuthorizationUrl(): String

  def fetchToken(code: String): Option[String]

  def getUserInfo(accessToken: String): Option[UserInfo]
}
