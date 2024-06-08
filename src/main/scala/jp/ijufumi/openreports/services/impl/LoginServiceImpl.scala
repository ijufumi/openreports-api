package jp.ijufumi.openreports.services.impl

import jp.ijufumi.openreports.services.{LoginService, WorkspaceService}
import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.utils.{Hash, IDs, Logging, Strings}
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.{
  MemberRepository,
  RefreshTokenRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.infrastructure.google.auth.GoogleRepository
import jp.ijufumi.openreports.presentation.models.requests.{GoogleLogin, Login}
import jp.ijufumi.openreports.presentation.models.responses.Member
import jp.ijufumi.openreports.domain.models.entity.{
  Member => MemberModel,
  RefreshToken => RefreshTokenModel,
}
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.domain.models.entity.Member.conversions._
import jp.ijufumi.openreports.domain.models.entity.Workspace.conversions._
import slick.jdbc.JdbcBackend.Database

@Singleton
class LoginServiceImpl @Inject() (
    db: Database,
    memberRepository: MemberRepository,
    workspaceRepository: WorkspaceRepository,
    googleRepository: GoogleRepository,
    workspaceService: WorkspaceService,
    refreshTokenRepository: RefreshTokenRepository,
) extends LoginService
    with Logging {
  private val regexBearerHeader = java.util.regex.Pattern.compile("^Bearer (.*)$")

  override def login(input: Login): Option[Member] = {
    val email = input.email
    val password = input.password
    val memberOpt = memberRepository.getMemberByEmail(db, email)
    if (memberOpt.isEmpty) {
      logger.info(s"$email does not exist")
      return None
    }
    val hashedPassword = Hash.hmacSha256(password)
    val member = memberOpt.get
    if (hashedPassword != member.password) {
      logger.info(s"$email's password does not match")
      return None
    }
    makeResponse(member)
  }

  override def logout(authorizationHeader: String): Unit = {
    val memberOpt = getMember(authorizationHeader)
    if (memberOpt.isEmpty) {
      return
    }

  }

  override def verifyApiToken(authorizationHeader: String): Option[Member] = {
    val apiToken = getApiToken(authorizationHeader)
    if (apiToken.isEmpty) {
      logger.info("api token is empty")
      return None
    }
    val memberId = Hash.extractIdFromJWT(apiToken.get)
    if (memberId == "") {
      logger.info("didn't extract member id from token")
      return None
    }

    val memberOpt = memberRepository.getById(db, memberId)
    if (memberOpt.isEmpty) {
      logger.info("member doesn't exist")
      return None
    }
    makeResponse(memberOpt.get)
  }

  override def getAuthorizationUrl: String = googleRepository.getAuthorizationUrl()

  override def loginWithGoogle(input: GoogleLogin): Option[Member] = {
    val tokenOpt = googleRepository.fetchToken(input.code)
    if (tokenOpt.isEmpty) {
      logger.info("Missing token")
      return None
    }
    val userInfoOpt = googleRepository.getUserInfo(tokenOpt.get)
    if (userInfoOpt.isEmpty) {
      logger.info("Missing userInfo")
      return None
    }

    val userInfo = userInfoOpt.get

    val memberOptById = memberRepository.getByGoogleId(db, userInfo.id)
    if (memberOptById.isDefined) {
      return makeResponse(memberOptById.get)
    }

    val memberOptByEmail = memberRepository.getMemberByEmail(db, userInfo.email)
    if (memberOptByEmail.isDefined) {
      val member = memberOptByEmail.get
      val newMember = member.copy(googleId = Some(userInfo.id))
      memberRepository.update(db, newMember)
      return makeResponse(newMember)
    }

    val member = MemberModel(
      id = IDs.ulid(),
      googleId = Some(userInfo.id),
      email = userInfo.email,
      password = "",
      name = userInfo.name,
    )

    try {
      val newMemberOpt = memberRepository.register(db, member)
      val workspaceName = Strings.nameFromEmail(member.email) + "'s workspace"
      workspaceService.createAndRelevant(workspaceName, member.id)
      makeResponse(newMemberOpt.get)
    } catch {
      case e: Throwable =>
        SimpleDBIO(_.connection.rollback()).withPinnedSession
        throw e
    }
  }

  def generateAccessToken(memberId: String, refreshToken: String): Option[String] = {
    val refreshToken = refreshTokenRepository.getByToken(db, refreshToken)
    if (refreshToken.isEmpty) {
      return None
    }

    val apiToken = Hash.generateJWT(memberId, Config.ACCESS_TOKEN_EXPIRATION_SEC)
    Some(apiToken)
  }

  override def generateRefreshToken(memberId: String): String = {
    val token = Hash.generateJWT(memberId, Config.REFRESH_TOKEN_EXPIRATION_SEC)
    val refreshToken = RefreshTokenModel(
      IDs.ulid(),
      memberId,
      token,
      isUsed = false,
    )
    refreshTokenRepository.create(db, refreshToken)
    token
  }

  private def makeResponse(member: Member): Option[Member] = {
    val workspaces = workspaceRepository.getsByMemberId(db, member.id)
    Some(
      member.withWorkspace(workspaces),
    )
  }

  private def getApiToken(authorizationHeader: String): Option[String] = {
    if (authorizationHeader == null || authorizationHeader.isEmpty) {
      return None
    }

    val tokenMatcher = regexBearerHeader.matcher(authorizationHeader)
    if (!tokenMatcher.matches()) {
      return None
    }
    Some(tokenMatcher.group(1))
  }

  private def getMember(authorizationHeader: String): Option[Member] = {
    val apiToken = getApiToken(authorizationHeader)
    if (apiToken.isEmpty) {
      return None
    }
    val memberId = Hash.extractIdFromJWT(apiToken.get)
    if (memberId == "") {
      return None
    }
    memberRepository.getById(db, memberId)
  }
}
