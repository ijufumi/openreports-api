package jp.ijufumi.openreports.infrastructure.google.auth

import jp.ijufumi.openreports.infrastructure.google.auth.models.UserInfo

trait GoogleRepository {
  def getAuthorizationUrl(): String

  def fetchToken(code: String): Option[String]

  def getUserInfo(accessToken: String): Option[UserInfo]
}
