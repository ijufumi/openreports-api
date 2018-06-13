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
  members: Seq[TMember] = Nil,
  functions : Seq[TFunction] = Nil,
)

object TGroup extends SkinnyCRUDMapper[TGroup]
    with OptimisticLockWithVersionFeature[TGroup] {

  override def tableName = "t_group"

  override def defaultAlias = createAlias("grp")

  override def primaryKeyFieldName = "groupId"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet, n: ResultName[TGroup]): TGroup = new TGroup(
    groupId = rs.get(n.groupId),
    groupName = rs.get(n.groupName),
    versions = rs.get(n.versions),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )

  lazy val members = hasManyThroughWithFk[TMember](
    through = RMemberGroup,
    many = TMember,
    throughFk = "groupId",
    manyFk = "memberId",
    merge = (a, members) => a.copy(members = members)
  ).includes[TMember]((mg, members) => mg.map { m => m.copy(members = members.filter(_.groups.exists(_.groupId == m.groupId))) })

  hasManyThroughWithFk[TFunction](
    through = RGroupFunction,
    many = TFunction,
    throughFk = "groupId",
    manyFk = "function_id",
    merge = (g, functions) => g.copy(functions = functions)
  ).byDefault
}