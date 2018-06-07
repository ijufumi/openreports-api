package jp.ijufumi.openreports.model

import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyNoIdCRUDMapper

case class RMemberGroup(memberId: Long, groupId: Long)

object RMemberGroup extends SkinnyNoIdCRUDMapper[RMemberGroup] {
  override def tableName = "r_member_group"

  override def defaultAlias = createAlias("mem_grp")

  override def extract(rs: WrappedResultSet, n: ResultName[RMemberGroup]): RMemberGroup = new RMemberGroup(
    memberId = rs.get(n.memberId),
    groupId = rs.get(n.groupId)
  )
}
