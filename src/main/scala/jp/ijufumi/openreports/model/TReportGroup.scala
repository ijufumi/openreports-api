package jp.ijufumi.openreports.model

import java.time.LocalDateTime

import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TReportGroup(reportGroupId: Long,
                        reportGroupName: String,
                        createdAt: LocalDateTime,
                        updatedAt: LocalDateTime,
                        versions: Long,
                        reports: Seq[TReport] = Nil,
                        groups: Seq[TGroup] = Nil)

object TReportGroup
    extends SkinnyCRUDMapper[TReportGroup]
    with OptimisticLockWithVersionFeature[TReportGroup] {

  lazy val reports = hasManyThroughWithFk[TReport](
    through = RReportReportGroup,
    many = TReport,
    throughFk = "reportGroupId",
    manyFk = "reportId",
    merge = (a, reports) => a.copy(reports = reports)
  ).includes[TReport](
    (rg, reps) =>
      rg.map { m =>
        m.copy(reports = reps)
    }
  )
  lazy val groups = hasManyThroughWithFk[TGroup](
    through = RGroupReportGroup,
    many = TGroup,
    throughFk = "reportGroupId",
    manyFk = "groupId",
    merge = (a, groups) => a.copy(groups = groups)
  ).includes[TGroup](
    (rg, grps) =>
      rg.map { m =>
        m.copy(groups = grps)
    }
  )

  override def tableName = "t_report_group"

  override def defaultAlias = createAlias("rep_grp")

  override def primaryKeyFieldName = "reportGroupId"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet,
                       n: ResultName[TReportGroup]): TReportGroup =
    new TReportGroup(
      reportGroupId = rs.get(n.reportGroupId),
      reportGroupName = rs.get(n.reportGroupName),
      createdAt = rs.get(n.createdAt),
      updatedAt = rs.get(n.updatedAt),
      versions = rs.get(n.versions)
    )
}
