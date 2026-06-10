package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.usecase.port.input.param.{GoogleLoginInput, LoginInput}
import jp.ijufumi.openreports.domain.models.entity.{Member => MemberModel}
import jp.ijufumi.openreports.domain.models.value.AuthTokens

trait LoginUseCase {
  def login(input: LoginInput): Option[MemberModel]

  def logout(authorizationHeader: String, refreshToken: String): Unit

  def verifyAuthorizationHeader(authorizationHeader: String): Option[MemberModel]

  def verifyApiToken(apiToken: String): Option[MemberModel]

  def verifyWorkspaceId(memberId: String, workspaceId: String): Boolean

  def getAuthorizationUrl: String

  def loginWithGoogle(input: GoogleLoginInput): Option[MemberModel]

  def generateTokens(memberId: String): AuthTokens

  def refreshTokens(refreshToken: String): Option[AuthTokens]
}
