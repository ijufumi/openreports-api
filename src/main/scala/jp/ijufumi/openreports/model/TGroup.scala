package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TGroup(
    groupId: Long,
    groupName: String,
    createdAt: DateTime,
    updatedAt: DateTime,
    versions: Long,
    members: Seq[TMember] = Nil,
    functions: Seq[TFunction] = Nil,
    reportGroups: Seq[TReportGroup] = Nil,
)

object TGroup
    extends SkinnyCRUDMapper[TGroup]
    with OptimisticLockWithVersionFeature[TGroup] {

  lazy val members = hasManyThroughWithFk[TMember](
    through = RMemberGroup,
    many = TMember,
    throughFk = "groupId",
    manyFk = "memberId",
    merge = (
        a,
        members
    ) => a.copy(members = members)
  ).includes[TMember](
    (
        mg,
        mems
    ) =>
      mg.map { m =>
        m.copy(members = mems)
    }
  )
  lazy val functions = hasManyThroughWithFk[TFunction](
    through = RGroupFunction,
    many = TFunction,
    throughFk = "groupId",
    manyFk = "functionId",
    merge = (
        g,
        functions
    ) => g.copy(functions = functions)
  ).includes[TFunction](
    (
        groups,
        func
    ) =>
      groups.map { g =>
        g.copy(functions = func)
    }
  )
  lazy val reportGroups = hasManyThroughWithFk[TReportGroup](
    through = RGroupReportGroup,
    many = TReportGroup,
    throughFk = "groupId",
    manyFk = "reportGroupId",
    merge = (
        g,
        reportGroups
    ) => g.copy(reportGroups = reportGroups)
  ).includes[TReportGroup](
    (
        groups,
        repgrps
    ) =>
      groups.map { g =>
        g.copy(reportGroups = repgrps)
    }
  )

  override def tableName = "t_group"

  override def defaultAlias = createAlias("grp")

  override def primaryKeyFieldName = "groupId"

  override def lockVersionFieldName: String = "versions"

  override def extract(
      rs: WrappedResultSet,
      n: ResultName[TGroup]
  ): TGroup =
    new TGroup(
      groupId = rs.get(n.groupId),
      groupName = rs.get(n.groupName),
      versions = rs.get(n.versions),
      createdAt = rs.get(n.createdAt),
      updatedAt = rs.get(n.updatedAt)
    )
}
