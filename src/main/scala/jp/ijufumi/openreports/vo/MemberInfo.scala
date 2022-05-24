package jp.ijufumi.openreports.vo

import jp.ijufumi.openreports.model.TMember

import scala.beans.BeanProperty
import scala.collection.Seq

case class MemberInfo(@BeanProperty memberId: Long,
                      @BeanProperty name: String,
                      @BeanProperty emailAddress: String,
                      @BeanProperty groups: Seq[GroupInfo],
                      @BeanProperty menus: Seq[Long],
                      @BeanProperty versions: Long) {

  def this(member: TMember, groups: Seq[GroupInfo]) = {
    this(
      member.memberId,
      member.name,
      member.emailAddress,
      groups,
      Seq.empty,
      member.versions
    )
  }
}

object MemberInfo {
  def apply(member: TMember): MemberInfo = {
    new MemberInfo(member, member.groups.map { g =>
      GroupInfo(g.groupId, g.groupName, g.versions, true)
    })
  }
}
