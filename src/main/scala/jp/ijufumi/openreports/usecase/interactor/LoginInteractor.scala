package jp.ijufumi.openreports.usecase.interactor

import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, WorkspaceUseCase}
import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.domain.port.{AppConfigPort, CacheKeys, CachePort, GoogleAuthPort}
import jp.ijufumi.openreports.utils.{Dates, Hash, IDs, Logging, Strings}
import jp.ijufumi.openreports.domain.repository.{
  MemberRepository,
  RefreshTokenRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.usecase.port.input.param.{GoogleLoginInput, LoginInput}
import jp.ijufumi.openreports.domain.models.entity.{
  Member => MemberModel,
  RefreshToken => RefreshTokenModel,
}
import jp.ijufumi.openreports.domain.models.value.AuthTokens
import slick.jdbc.JdbcBackend.Database

@Singleton
class LoginInteractor @Inject() (
    db: Database,
    memberRepository: MemberRepository,
    workspaceRepository: WorkspaceRepository,
    refreshTokenRepository: RefreshTokenRepository,
    googleAuthPort: GoogleAuthPort,
    workspaceService: WorkspaceUseCase,
    cachePort: CachePort,
    appConfig: AppConfigPort,
) extends LoginUseCase
    with Logging {
  private val regexBearerHeader = java.util.regex.Pattern.compile("^Bearer (.*)$")

  override def login(input: LoginInput): Option[MemberModel] = {
    val email = input.email
    val password = input.password
    val memberOpt = memberRepository.getMemberByEmail(db, email)
    if (memberOpt.isEmpty) {
      logger.info(s"$email does not exist")
      return None
    }
    val member = memberOpt.get
    if (!Hash.verifyPassword(password, member.password)) {
      logger.info(s"$email's password does not match")
      return None
    }
    makeResponse(member)
  }

  override def logout(memberId: String, refreshTokens: Seq[String]): Unit = {
    refreshTokens.foreach { refreshToken =>
      if (refreshToken != null && refreshToken.nonEmpty) {
        val tokenMemberId = Hash.extractIdFromJWT(refreshToken)
        if (tokenMemberId == memberId) {
          refreshTokenRepository.deleteByToken(db, Hash.hmacSha256(refreshToken))
        } else {
          logger.info("refresh token does not belong to the member")
        }
      }
    }
  }

  override def verifyAuthorizationHeader(authorizationHeader: String): Option[MemberModel] = {
    val apiToken = getApiToken(authorizationHeader)
    if (apiToken.isEmpty) {
      logger.info("api token is empty")
      return None
    }
    verifyApiToken(apiToken.get)
  }

  override def verifyApiToken(apiToken: String): Option[MemberModel] = {
    val memberId = Hash.extractIdFromJWT(apiToken)
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

  override def verifyWorkspaceId(memberId: String, workspaceId: String): Boolean = {
    workspaceService.getWorkspaceMember(workspaceId, memberId).isDefined
  }

  override def getAuthorizationUrl: String = {
    val state = Strings.generateRandomString(32)()
    cachePort.put(CacheKeys.GoogleAuthState, state, state)(
      Config.GOOGLE_AUTH_STATE_CACHE_TTL_SEC,
    )
    googleAuthPort.getAuthorizationUrl(state)
  }

  override def loginWithGoogle(input: GoogleLoginInput): Option[MemberModel] = {
    if (input.state == null || input.state.isEmpty) {
      logger.info("OAuth state is missing")
      return None
    }
    val cachedStateOpt = cachePort.get(CacheKeys.GoogleAuthState, input.state)
    if (cachedStateOpt.isEmpty || cachedStateOpt.get != input.state) {
      logger.warn("OAuth state verification failed")
      return None
    }
    cachePort.remove(CacheKeys.GoogleAuthState, input.state)

    val tokenOpt = googleAuthPort.fetchToken(input.code)
    if (tokenOpt.isEmpty) {
      logger.info("Missing token")
      return None
    }
    val userInfoOpt = googleAuthPort.getUserInfo(tokenOpt.get)
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

    val newMemberOpt = memberRepository.register(db, member)
    try {
      val workspaceName = Strings.nameFromEmail(member.email) + "'s workspace"
      workspaceService.createAndRelevant(workspaceName, member.id)
      makeResponse(newMemberOpt.get)
    } catch {
      case e: Throwable =>
        logger.error(s"Failed to create workspace for member ${member.id}; rolling back member", e)
        try {
          memberRepository.delete(db, member.id)
        } catch {
          case cleanupError: Throwable =>
            logger.error(s"Failed to roll back member ${member.id}", cleanupError)
        }
        throw e
    }
  }

  override def generateTokens(memberId: String): AuthTokens = {
    val accessToken = Hash.generateJWT(memberId, appConfig.accessTokenExpirationSec)
    val refreshToken = Hash.generateJWT(memberId, appConfig.refreshTokenExpirationSec)
    val now = Dates.currentTimestamp()
    refreshTokenRepository.deleteExpired(db, now)
    refreshTokenRepository.register(
      db,
      RefreshTokenModel(
        id = IDs.ulid(),
        memberId = memberId,
        refreshToken = Hash.hmacSha256(refreshToken),
        expiredAt = now + appConfig.refreshTokenExpirationSec.toLong * 1000,
      ),
    )
    AuthTokens(accessToken, Some(refreshToken))
  }

  override def refreshTokens(refreshToken: String): Option[AuthTokens] = {
    val memberId = Hash.extractIdFromJWT(refreshToken)
    if (memberId == null || memberId.isEmpty) {
      logger.info("refresh token is invalid")
      return None
    }

    val hashedToken = Hash.hmacSha256(refreshToken)
    val storedTokenOpt = refreshTokenRepository.getByToken(db, hashedToken)
    if (storedTokenOpt.isEmpty) {
      logger.info("refresh token is not registered")
      return None
    }

    val storedToken = storedTokenOpt.get
    val now = Dates.currentTimestamp()
    if (storedToken.memberId != memberId || storedToken.expiredAt < now) {
      refreshTokenRepository.deleteByToken(db, hashedToken)
      logger.info("refresh token is expired or member does not match")
      return None
    }

    // rotate: only the request that marks the token as used can issue a new refresh token,
    // so concurrent refreshes with the same token produce at most one new pair
    val claimed = refreshTokenRepository.markUsed(db, hashedToken, now)
    if (claimed > 0) {
      return Some(generateTokens(memberId))
    }

    // late concurrent request: reissue only an access token within the grace period
    val usedTokenOpt = refreshTokenRepository.getByToken(db, hashedToken)
    val withinGracePeriod =
      usedTokenOpt.flatMap(_.usedAt).exists(now - _ <= Config.REFRESH_TOKEN_GRACE_PERIOD_MILLIS)
    if (!withinGracePeriod) {
      logger.info("refresh token was already used")
      return None
    }
    Some(AuthTokens(Hash.generateJWT(memberId, appConfig.accessTokenExpirationSec), None))
  }

  private def makeResponse(member: MemberModel): Option[MemberModel] = {
    val workspaces = workspaceRepository.getsByMemberId(db, member.id)
    Some(
      member.copy(workspaces = workspaces),
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
}
