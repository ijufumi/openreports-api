package jp.ijufumi.openreports.services.impl

import jp.ijufumi.openreports.services.LoginService
import jp.ijufumi.openreports.repositories.system._
import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.utils.Hash
import jp.ijufumi.openreports.vo.response.MemberResponse
import jp.ijufumi.openreports.cache.{CacheKeys, CacheWrapper}
import jp.ijufumi.openreports.entities.Member
import jp.ijufumi.openreports.repositories.db._

@Singleton
class LoginServiceImpl @Inject() (
    cacheWrapper: CacheWrapper,
    memberRepository: MemberRepository,
    googleRepository: GoogleRepository,
) extends LoginService {
  override def login(email: String, password: String): Option[MemberResponse] = {
    val memberOpt = memberRepository.getMemberByEmail(email)
    if (memberOpt.isEmpty) {
      return Option.empty[MemberResponse]
    }
    val hashedPassword = Hash.hmacSha256(password)
    val member = memberOpt.get
    if (hashedPassword != member.password) {
      return Option.empty[MemberResponse]
    }
    makeResponse(member)
  }

  override def verifyApiToken(apiToken: String): Boolean = {
    val cachedApiToken = cacheWrapper.get[String](CacheKeys.ApiToken)

    cachedApiToken.getOrElse("").equals(apiToken)
  }

  override def getAuthorizationUrl: String = googleRepository.getAuthorizationUrl()

  override def loginWithGoogle(state: String, code: String): Option[MemberResponse] = {
    val tokenOpt = googleRepository.fetchToken(state, code)
    if (tokenOpt.isEmpty) {
      return Option.empty
    }
    val userInfoOpt = googleRepository.getUserInfo(tokenOpt.get)
    if (userInfoOpt.isEmpty) {
      return Option.empty
    }
    val userInfo = userInfoOpt.get
    val memberOpt = memberRepository.getMemberByEmail(userInfo.email)
    if (memberOpt.isDefined) {
      return makeResponse(memberOpt.get)
    }

    // TODO: implements
    Option.empty
  }

  private def makeResponse(member: Member): Option[MemberResponse] = {
    val apiToken = Hash.generateJWT(member.id, Config.API_TOKEN_EXPIRATION_SEC)
    cacheWrapper.put(CacheKeys.ApiToken, apiToken)
    Option.apply(
      MemberResponse(member.id, member.emailAddress, member.name, member.isAdmin, apiToken),
    )
  }
}
