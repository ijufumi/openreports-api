package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyCRUDMapper

case class TGroup(groupId: Long,
                  groupName: String,
                  createdAt: DateTime,
                  updatedAt: DateTime,
                  members: Seq[TMember] = Nil)

object TGroup extends SkinnyCRUDMapper[TGroup] {
  override def tableName = "t_group"

  override def defaultAlias = createAlias("grp")

  override def primaryKeyFieldName = "group_id"

  override def extract(rs: WrappedResultSet, n: ResultName[TGroup]): TGroup = new TGroup(
    groupId = rs.get(n.groupId),
    groupName = rs.get(n.groupName),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )

  hasManyThrough[TMember](
    through = RMemberGroup,
    many = TMember,
    merge = (a, members) => a.copy(members = members)).byDefault
}