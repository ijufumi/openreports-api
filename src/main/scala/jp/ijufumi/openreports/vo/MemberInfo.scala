package jp.ijufumi.openreports.vo

import jp.ijufumi.openreports.model.TMember

case class MemberInfo(
  memberId: Long,
  name: String,
  groups: Seq[Long],
  menus: Seq[Long]
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
