package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.models.outputs.Member

trait LoginService {
  def login(email: String, password: String): Option[Member]

  def logout(apiToken: String): Unit

  def verifyApiToken(authorizationHeader: String): Boolean

  def getAuthorizationUrl: String

  def loginWithGoogle(code: String): Option[Member]

  def getMemberByToken(authorizationHeader: String): Option[Member]
}
