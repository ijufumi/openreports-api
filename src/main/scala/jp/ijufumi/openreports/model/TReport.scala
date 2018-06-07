package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.WrappedResultSet
import skinny.orm.SkinnyCRUDMapper

case class TReport(reportId: Long, reportName: String, templatePath: String, createdAt: DateTime, updatedAt: DateTime)

object TReport extends SkinnyCRUDMapper[TReport] {
  override def tableName = "t_report"

  override def defaultAlias = createAlias("rep")

  override def primaryKeyFieldName = "report_id"

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[TReport]): TReport = new TReport(
    reportId = rs.get(n.reportId),
    reportName = rs.get(n.reportName),
    templatePath = rs.get(n.templatePath),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}