package jp.ijufumi.openreports.model

import jp.ijufumi.openreports.model.TMember.hasManyThroughWithFk
import org.joda.time.DateTime
import scalikejdbc.{ ResultName, WrappedResultSet }
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TGroup(
  groupId: Long,
  groupName: String,
  createdAt: DateTime,
  updatedAt: DateTime,
  versions: Long,
  members: Seq[TMember] = Nil
)

object TGroup extends SkinnyCRUDMapper[TGroup]
    with OptimisticLockWithVersionFeature[TGroup] {

  override def tableName = "t_group"

  override def defaultAlias = createAlias("grp")

  override def primaryKeyFieldName = "group_id"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet, n: ResultName[TGroup]): TGroup = new TGroup(
    groupId = rs.get(n.groupId),
    groupName = rs.get(n.groupName),
    versions = rs.get(n.versions),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}