package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.{ ResultName, WrappedResultSet }
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TReportGroup(
  reportGroupId: Long,
  reportGroupName: String,
  createdAt: DateTime,
  updatedAt: DateTime,
  reports: Seq[TReport] = Nil,
  groups: Seq[TGroup] = Nil
)

object TReportGroup extends SkinnyCRUDMapper[TReportGroup]
    with OptimisticLockWithVersionFeature[TReportGroup] {

  override def tableName = "t_report_group"

  override def defaultAlias = createAlias("rep_grp")

  override def primaryKeyFieldName = "reportGroupId"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet, n: ResultName[TReportGroup]): TReportGroup = new TReportGroup(
    reportGroupId = rs.get(n.reportGroupId),
    reportGroupName = rs.get(n.reportGroupName),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )

  lazy val reports = hasManyThroughWithFk[TReport](
    through = RGroupReportGroup,
    many = TReport,
    throughFk = "reportId",
    manyFk = "reportGroupId",
    merge = (a, reports) => a.copy(reports = reports)
  ).includes[TReport]((rg, reps) => rg.map { m => m.copy(reports = reps) })

  lazy val groups = hasManyThroughWithFk[TGroup](
    through = RGroupReportGroup,
    many = TGroup,
    throughFk = "",
    manyFk = "",
    merge = (a, groups) => a.copy(groups = groups)
  ).includes[TGroup]((rg, grps) => rg.map { m => m.copy(groups = grps) })
}
