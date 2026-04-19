package jp.ijufumi.openreports.infrastructure.external.google

import jp.ijufumi.openreports.infrastructure.external.google.models.UserInfo

trait GoogleRepository {
  def getAuthorizationUrl(state: String): String

  def fetchToken(code: String): Option[String]

  def getUserInfo(accessToken: String): Option[UserInfo]
}
