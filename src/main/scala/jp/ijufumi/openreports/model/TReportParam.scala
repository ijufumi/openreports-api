package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.{ ResultName, WrappedResultSet }
import skinny.orm.feature.OptimisticLockWithVersionFeature
import skinny.orm.{ Alias, SkinnyCRUDMapper }

case class TReportParam(
  paramId: Long,
  paramKey: String,
  paramName: String,
  description: String,
  paramType: String,
  paramValues: String,
  createdAt: DateTime,
  updatedAt: DateTime,
  versions: Long,
  reports: Seq[TReport] = Nil
)

object TReportParam extends SkinnyCRUDMapper[TReportParam]
    with OptimisticLockWithVersionFeature[TReportParam] {

  override def tableName: String = "t_report_param"

  override def defaultAlias: Alias[TReportParam] = createAlias("param")

  override def primaryKeyFieldName: String = "paramId"

  override def lockVersionFieldName: String = "versions"

  override def extract(rs: WrappedResultSet, n: ResultName[TReportParam]): TReportParam = new TReportParam(
    paramId = rs.get(n.paramId),
    paramKey = rs.get(n.paramKey),
    paramName = rs.get(n.paramName),
    description = rs.get(n.description),
    paramType = rs.get(n.paramType),
    paramValues = rs.get(n.paramValues),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt),
    versions = rs.get(n.versions)
  )
}