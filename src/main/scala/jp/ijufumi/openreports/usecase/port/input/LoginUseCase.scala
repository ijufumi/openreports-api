package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.usecase.port.input.param.{LoginInput, GoogleLoginInput}
import jp.ijufumi.openreports.domain.models.entity.{Member => MemberModel}

trait LoginUseCase {
  def login(input: LoginInput): Option[MemberModel]

  def logout(apiToken: String): Unit

  def verifyAuthorizationHeader(authorizationHeader: String): Option[MemberModel]

  def verifyApiToken(apiToken: String): Option[MemberModel]

  def verifyWorkspaceId(memberId: String, workspaceId: String): Boolean

  def getAuthorizationUrl: String

  def loginWithGoogle(input: GoogleLoginInput): Option[MemberModel]

  def generateAccessToken(refreshToken: String): Option[String]

  def generateRefreshToken(memberId: String): String
}
