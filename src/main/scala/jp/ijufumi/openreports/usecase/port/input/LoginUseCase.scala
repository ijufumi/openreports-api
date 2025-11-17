package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.presentation.request.{GoogleLogin, Login}
import jp.ijufumi.openreports.presentation.responses.Member

trait LoginUseCase {
  def login(input: Login): Option[Member]

  def logout(apiToken: String): Unit

  def verifyAuthorizationHeader(authorizationHeader: String): Option[Member]

  def verifyApiToken(apiToken: String): Option[Member]

  def verifyWorkspaceId(memberId: String, workspaceId: String): Boolean

  def getAuthorizationUrl: String

  def loginWithGoogle(input: GoogleLogin): Option[Member]

  def generateAccessToken(refreshToken: String): Option[String]

  def generateRefreshToken(memberId: String): String
}
