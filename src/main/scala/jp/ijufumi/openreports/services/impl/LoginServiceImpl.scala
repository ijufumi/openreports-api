package jp.ijufumi.openreports.services.impl

import jp.ijufumi.openreports.services.{LoginService, WorkspaceService}
import jp.ijufumi.openreports.repositories.system._
import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.utils.{Hash, IDs, Logging}
import jp.ijufumi.openreports.vo.response.{Member => MemberReponse}
import jp.ijufumi.openreports.cache.{CacheKeys, CacheWrapper}
import jp.ijufumi.openreports.entities._
import jp.ijufumi.openreports.repositories.db._
import slick.jdbc.PostgresProfile.api._

@Singleton
class LoginServiceImpl @Inject() (
    cacheWrapper: CacheWrapper,
    memberRepository: MemberRepository,
    workspaceRepository: WorkspaceRepository,
    googleRepository: GoogleRepository,
    workspaceService: WorkspaceService,
) extends LoginService
    with Logging {
  override def login(email: String, password: String): Option[MemberReponse] = {
    val memberOpt = memberRepository.getMemberByEmail(email)
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

    cacheWrapper.remove(CacheKeys.ApiToken, memberOpt.get.id)
  }

  override def verifyApiToken(authorizationHeader: String): Boolean = {
    val apiToken = getApiToken(authorizationHeader)
    if (apiToken.isEmpty) {
      return false
    }
    val memberId = Hash.extractIdFromJWT(apiToken.get)
    if (memberId == "") {
      return false
    }

    val cachedApiToken = cacheWrapper.get[String](CacheKeys.ApiToken, memberId)

    cachedApiToken.getOrElse("").equals(apiToken.get)
  }

  override def getAuthorizationUrl: String = googleRepository.getAuthorizationUrl()

  override def loginWithGoogle(code: String): Option[MemberReponse] = {
    val tokenOpt = googleRepository.fetchToken(code)
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

    val memberOptById = memberRepository.getByGoogleId(userInfo.id)
    if (memberOptById.isDefined) {
      return makeResponse(memberOptById.get)
    }

    val memberOptByEmail = memberRepository.getMemberByEmail(userInfo.email)
    if (memberOptByEmail.isDefined) {
      val member = memberOptByEmail.get
      val newMember = member.copy(googleId = Some(userInfo.id))
      memberRepository.update(newMember)
      return makeResponse(newMember)
    }

    val member = Member(
      id = IDs.ulid(),
      googleId = Some(userInfo.id),
      email = userInfo.email,
      name = userInfo.name,
    )

    try {
      val newMemberOpt = memberRepository.register(member)
      workspaceService.createAndRelevant(member.id, member.email)
      makeResponse(newMemberOpt.get)
    } catch {
      case e: Throwable =>
        SimpleDBIO(_.connection.rollback()).withPinnedSession
        throw e
    }
  }

  def getMemberByToken(authorizationHeader: String): Option[MemberReponse] = {
    val memberOpt = getMember(authorizationHeader)
    if (memberOpt.isEmpty) {
      return None
    }
    makeResponse(memberOpt.get)
  }

  private def makeResponse(member: Member): Option[MemberReponse] = {
    val apiToken = Hash.generateJWT(member.id, Config.API_TOKEN_EXPIRATION_SEC)
    cacheWrapper.put(CacheKeys.ApiToken, apiToken, member.id)
    val workspaces = workspaceRepository.getsByMemberId(member.id)
    Some(
      MemberReponse(member, workspaces, apiToken),
    )
  }

  private def getApiToken(authorizationHeader: String): Option[String] = {
    if (authorizationHeader == null || authorizationHeader.isEmpty) {
      return None
    }

    val splitHeader = authorizationHeader.split(" ")
    if (splitHeader.length != 2) {
      return None
    }
    Some(splitHeader(1))
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
    memberRepository.getById(memberId)
  }
}
