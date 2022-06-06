package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model.TMember
import jp.ijufumi.openreports.service.support.Hash
import jp.ijufumi.openreports.vo.MemberInfo
import skinny.LoggerProvider

trait LoginService {
  def login(emailAddress: String, password: String): Option[MemberInfo]
}

class LoginServiceImpl extends LoginService with LoggerProvider {
  override def login(emailAddress: String, password: String): Option[MemberInfo] = {
    var member: MemberInfo = null
    val hashedPassword = Hash.hmacSha256(HashKey, password)
    val members: Seq[TMember] = TMember
      .where("emailAddress" -> emailAddress, "password" -> hashedPassword)
      .apply()

    if (members.isEmpty) {
      logger.info(
        "invalid id or password : [%s][%s]".format(emailAddress, hashedPassword)
      )
    } else {
      val m = members.head
      member = MemberInfo(
        m.memberId,
        m.name,
        m.emailAddress,
        m.versions
      )
      logger.info("memberInfo:%s".format(member))
    }
    Option(member)
  }
}
