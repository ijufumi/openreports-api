package jp.ijufumi.openreports.vo

import scala.beans.BeanProperty

import jp.ijufumi.openreports.model.TMember

case class MemberInfo(
  @BeanProperty memberId: Long,
  @BeanProperty name: String,
  @BeanProperty groups: Seq[Long],
  @BeanProperty menus: Seq[Long]
) {

  def this(member: TMember) =  {
    this(member.memberId, member.name, Seq.empty, Seq.empty)
  }
}

object MemberInfo {
  def apply(member: TMember) : MemberInfo ={
    new MemberInfo(member)
  }
}
