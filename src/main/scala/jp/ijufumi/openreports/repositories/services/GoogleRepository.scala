package jp.ijufumi.openreports.repositories.services

import jp.ijufumi.openreports.vo.response.google.UserInfo

trait GoogleRepository {
  def getAuthorizationUrl(): String

  def fetchToken(code: String): Option[String]

  def getUserInfo(accessToken: String): Option[UserInfo]
}
