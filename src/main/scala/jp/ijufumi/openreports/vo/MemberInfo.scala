package jp.ijufumi.openreports.vo

import jp.ijufumi.openreports.model.TMember

import scala.beans.BeanProperty

case class MemberInfo(@BeanProperty memberId: Long,
                      @BeanProperty name: String,
                      @BeanProperty groups: Seq[Long],
                      @BeanProperty menus: Seq[Long],
                      versions: Long) {

  def this(member: TMember) = {
    this(member.memberId, member.name, Seq.empty, Seq.empty, member.versions)
  }
}

object MemberInfo {
  def apply(member: TMember): MemberInfo = {
    new MemberInfo(member)
  }
}
