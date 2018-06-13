package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.WrappedResultSet
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TReport(
  reportId: Long,
  reportName: String,
  templatePath: String,
  createdAt: DateTime,
  updatedAt: DateTime,
  params: Seq[TReportParam] = Nil,
  groups: Seq[TReportGroup] = Nil
)

object TReport extends SkinnyCRUDMapper[TReport]
    with OptimisticLockWithVersionFeature[TReport] {

  override def tableName = "t_report"

  override def defaultAlias = createAlias("rep")

  override def primaryKeyFieldName = "reportId"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[TReport]): TReport = new TReport(
    reportId = rs.get(n.reportId),
    reportName = rs.get(n.reportName),
    templatePath = rs.get(n.templatePath),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )

  hasManyThroughWithFk[TReportParam](
    through = RReportReportParam,
    many = TReportParam,
    throughFk = "reportId",
    manyFk = "paramId",
    merge = (a, params) => a.copy(params = params)
  ).byDefault

  hasManyThroughWithFk[TReportGroup](
    through = RGroupReportGroup,
    many = TReportGroup,
    throughFk = "reportId",
    manyFk = "reportGroupId",
    merge = (a, groups) => a.copy(groups = groups)
  ).byDefault
}