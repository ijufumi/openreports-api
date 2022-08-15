package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.MemberResponse

trait LoginService {
  def login(email: String, password: String): Option[MemberResponse]

  def logout(apiToken: String): Unit

  def verifyApiToken(apiToken: String): Boolean

  def getAuthorizationUrl: String

  def loginWithGoogle(code: String): Option[MemberResponse]

  def getMemberByToken(apiToken: String): Option[MemberResponse]
}
