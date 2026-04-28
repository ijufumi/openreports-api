package jp.ijufumi.openreports.usecase.interactor

import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, WorkspaceUseCase}
import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.domain.port.{AppConfigPort, CacheKeys, CachePort, GoogleAuthPort}
import jp.ijufumi.openreports.utils.{Hash, IDs, Logging, Strings}
import jp.ijufumi.openreports.domain.repository.{MemberRepository, WorkspaceRepository}
import jp.ijufumi.openreports.usecase.port.input.param.{GoogleLoginInput, LoginInput}
import jp.ijufumi.openreports.domain.models.entity.{Member => MemberModel}
import slick.jdbc.JdbcBackend.Database

@Singleton
class LoginInteractor @Inject() (
    db: Database,
    memberRepository: MemberRepository,
    workspaceRepository: WorkspaceRepository,
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

  override def logout(authorizationHeader: String): Unit = {
    val memberOpt = getMember(authorizationHeader)
    if (memberOpt.isEmpty) {
      return
    }
    cachePort.remove(CacheKeys.ApiToken, memberOpt.get.id)
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

  def generateAccessToken(token: String): Option[String] = {
    val memberIdOpt = cachePort.get(CacheKeys.ApiToken, token)
    if (memberIdOpt.isEmpty) {
      return None
    }

    val apiToken = Hash.generateJWT(memberIdOpt.get, appConfig.accessTokenExpirationSec)
    Some(apiToken)
  }

  override def generateRefreshToken(memberId: String): String = {
    val token = Hash.generateJWT(memberId, appConfig.refreshTokenExpirationSec)
    cachePort.put(CacheKeys.ApiToken, memberId, token)(appConfig.refreshTokenExpirationSec.toLong)
    token
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

  private def getMember(authorizationHeader: String): Option[MemberModel] = {
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
