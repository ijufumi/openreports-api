package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.google.UserInfoResponse

trait GoogleService {
  def getAuthorizationUrl(): String

  def fetchToken(code: String): Option[String]

  def getUserInfo(accessToken: String): Option[UserInfoResponse]
}
