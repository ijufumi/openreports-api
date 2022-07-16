package jp.ijufumi.openreports.services.impl

import jp.ijufumi.openreports.services.LoginService
import jp.ijufumi.openreports.repositories._
import com.google.inject.{Inject, Singleton}
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.utils.Hash
import jp.ijufumi.openreports.vo.response.MemberResponse

@Singleton
class LoginServiceImpl @Inject() (memberRepository: MemberRepository) extends LoginService {
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
    val apiToken = Hash.generateJWT(member.id, Config.API_TOKEN_EXPIRATION_SEC)
    Option.apply(
      MemberResponse(member.id, member.emailAddress, member.name, member.isAdmin, apiToken),
    )
  }
}
