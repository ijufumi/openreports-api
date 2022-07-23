package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.google.UserInfoResponse

trait GoogleLoginService {
  def getAuthorizationUrl(): String

  def fetchToken(state: String, code: String): Option[String]

  def getUserInfo(accessToken: String): Option[UserInfoResponse]
}
