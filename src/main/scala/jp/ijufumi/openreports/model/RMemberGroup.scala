package jp.ijufumi.openreports.model

import skinny.SkinnyJoinTable

case class RMemberGroup(memberId: Long, groupId: Long)

object RMemberGroup extends SkinnyJoinTable[RMemberGroup] {
  override def tableName = "r_member_group"

  override def defaultAlias = createAlias("mem_grp")

}
