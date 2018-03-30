package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.WrappedResultSet
import skinny.orm.SkinnyCRUDMapper

case class Report(reportId: Long, reportName: String, templatePath: String, createdAt: DateTime, updatedAt: DateTime)

object Report extends SkinnyCRUDMapper[Report] {
  override def tableName = "report"
  override def defaultAlias = createAlias("rep")
  override def primaryKeyFieldName = "report_id"

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[Report]): Report = new Report(
    reportId = rs.get(n.reportId),
    reportName = rs.get(n.reportName),
    templatePath = rs.get(n.templatePath),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}