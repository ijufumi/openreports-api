package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.{ ResultName, WrappedResultSet }
import skinny.orm.feature.OptimisticLockWithVersionFeature
import skinny.orm.{ Alias, SkinnyCRUDMapper }

case class TReportParam(
  paramId: Long,
  paramName: String,
  paramType: String,
  createdAt: DateTime,
  updatedAt: DateTime,
  reports: Seq[TReport] = Nil
)

object TReportParam extends SkinnyCRUDMapper[TReportParam]
    with OptimisticLockWithVersionFeature[TReportParam] {

  override def tableName: String = "t_report_param"

  override def defaultAlias: Alias[TReportParam] = createAlias("param")

  override def primaryKeyFieldName: String = "param_id"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet, n: ResultName[TReportParam]): TReportParam = new TReportParam(
    paramId = rs.get(n.paramId),
    paramName = rs.get(n.paramName),
    paramType = rs.get(n.paramType),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )

  hasManyThrough[TReport](
    through = RReportReportParam,
    many = TReport,
    merge = (a, reports) => a.copy(reports = reports)
  ).byDefault
}