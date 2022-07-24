package jp.ijufumi.openreports.repositories.system

import jp.ijufumi.openreports.vo.response.google.UserInfoResponse

trait GoogleRepository {
  def getAuthorizationUrl(): String

  def fetchToken(state: String, code: String): Option[String]

  def getUserInfo(accessToken: String): Option[UserInfoResponse]
}
