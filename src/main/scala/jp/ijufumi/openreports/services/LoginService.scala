package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.models.inputs.{GoogleLogin, Login}
import jp.ijufumi.openreports.models.outputs.Member

trait LoginService {
  def login(input: Login): Option[Member]

  def logout(apiToken: String): Unit

  def verifyApiToken(authorizationHeader: String): Boolean

  def getAuthorizationUrl: String

  def loginWithGoogle(input: GoogleLogin): Option[Member]

  def getMemberByToken(authorizationHeader: String): Option[Member]
}
