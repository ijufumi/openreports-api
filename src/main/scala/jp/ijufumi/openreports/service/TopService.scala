package jp.ijufumi.openreports.service

import jp.ijufumi.openreports.model.{TGroup, TMember}
import jp.ijufumi.openreports.service.support.Hash
import jp.ijufumi.openreports.vo.{GroupInfo, MemberInfo}
import skinny.LoggerProvider

import scala.collection.mutable

class TopService extends LoggerProvider {
  def login(emailAddress: String, password: String): Option[MemberInfo] = {
    var member: MemberInfo = null
    val hashedPassword = Hash.hmacSha256(HASHED_KEY, password)
    val members: Seq[TMember] = TMember
      .where('emailAddress -> emailAddress, 'password -> hashedPassword)
      .apply();

    if (members.isEmpty) {
      logger.info(
        "invalid id or password : [%s][%s]".format(emailAddress, hashedPassword)
      )
    } else {
      val menus = mutable.Set[Long]()
      val groups = mutable.Set[GroupInfo]()
      val m = members.head
      for (g <- m.groups) {
        val group = TGroup.includes(TGroup.functions).findById(g.groupId).get
        logger.debug("Group:%s".format(group))
        menus ++= group.functions.map(_.functionId).toSet
        groups += GroupInfo(g.groupId, g.groupName)
      }

      member = MemberInfo(
        m.memberId,
        m.name,
        m.emailAddress,
        Seq(groups.toSeq: _*),
        Seq(menus.toSeq: _*),
        m.versions
      )
      logger.info("memberInfo:%s".format(member))
    }
    Option(member)
  }
}
