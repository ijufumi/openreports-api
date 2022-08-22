package jp.ijufumi.openreports.services.impl

import jp.ijufumi.openreports.services.LoginService
import jp.ijufumi.openreports.repositories.system._
import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.utils.{Hash, ID, Logging}
import jp.ijufumi.openreports.vo.response.MemberResponse
import jp.ijufumi.openreports.cache.{CacheKeys, CacheWrapper}
import jp.ijufumi.openreports.entities.Member
import jp.ijufumi.openreports.repositories.db._

@Singleton
class LoginServiceImpl @Inject() (
    cacheWrapper: CacheWrapper,
    memberRepository: MemberRepository,
    googleRepository: GoogleRepository,
) extends LoginService
    with Logging {
  override def login(email: String, password: String): Option[MemberResponse] = {
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

  override def logout(apiToken: String): Unit = {
    val memberId = Hash.extractIdFromJWT(apiToken)
    if (memberId == "") {
      return
    }

    cacheWrapper.remove(CacheKeys.ApiToken, memberId)
  }

  override def verifyApiToken(apiToken: String): Boolean = {
    val memberOpt = getMember(apiToken)
    if (memberOpt.isEmpty) {
      return false
    }

    val memberId = memberOpt.get.id
    val cachedApiToken = cacheWrapper.get[String](CacheKeys.ApiToken, memberId)

    cachedApiToken.getOrElse("").equals(apiToken)
  }

  override def getAuthorizationUrl: String = googleRepository.getAuthorizationUrl()

  override def loginWithGoogle(code: String): Option[MemberResponse] = {
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
      id = ID.ulid(),
      googleId = Some(userInfo.id),
      email = userInfo.email,
      name = userInfo.name,
    )
    val newMemberOpt = memberRepository.register(member)
    makeResponse(newMemberOpt.get)
  }

  def getMemberByToken(apiToken: String): Option[MemberResponse] = {
    val memberOpt = getMember(apiToken)
    if (memberOpt.isEmpty) {
      return None
    }
    makeResponse(memberOpt.get)
  }

  private def makeResponse(member: Member): Option[MemberResponse] = {
    val apiToken = Hash.generateJWT(member.id, Config.API_TOKEN_EXPIRATION_SEC)
    cacheWrapper.put(CacheKeys.ApiToken, apiToken, member.id)
    Some(
      MemberResponse(member.id, member.email, member.name, apiToken),
    )
  }

  private def getMember(apiToken: String): Option[Member] = {
    val memberId = Hash.extractIdFromJWT(apiToken)
    if (memberId == "") {
      return None
    }
    memberRepository.getById(memberId)
  }
}
