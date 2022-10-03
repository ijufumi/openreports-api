package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.Member

trait LoginService {
  def login(email: String, password: String): Option[Member]

  def logout(apiToken: String): Unit

  def verifyApiToken(apiTokenHeader: String): Boolean

  def getAuthorizationUrl: String

  def loginWithGoogle(code: String): Option[Member]

  def getMemberByToken(apiToken: String): Option[Member]
}
