package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.MemberResponse

trait LoginService {
  def login(email: String, password: String): Option[MemberResponse]
  def getAuthorizationUrl: String
  def loginWithGoogle(state: String, code: String): Option[MemberResponse]
  def verifyApiToken(apiToken: String): Boolean
}
