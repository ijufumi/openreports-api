package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.presentation.models.requests.{GoogleLogin, Login}
import jp.ijufumi.openreports.presentation.models.responses.Member

trait LoginService {
  def login(input: Login): Option[Member]

  def logout(apiToken: String): Unit

  def verifyApiToken(authorizationHeader: String): Boolean

  def getAuthorizationUrl: String

  def loginWithGoogle(input: GoogleLogin): Option[Member]

  def getMemberByToken(authorizationHeader: String, generateToken: Boolean = true): Option[Member]
}
