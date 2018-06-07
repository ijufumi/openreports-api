package jp.ijufumi.openreports.model

import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyNoIdCRUDMapper

case class RReportReportParam(reportId: Long, paramId: Long)

object RReportReportParam extends SkinnyNoIdCRUDMapper[RReportReportParam] {
  override def tableName = "r_report_report_param"

  override def defaultAlias = createAlias("rep_rep_param")

  override def extract(rs: WrappedResultSet, n: ResultName[RReportReportParam]): RReportReportParam = new RReportReportParam(
    reportId = rs.get(n.reportId),
    paramId = rs.get(n.paramId)
  )
}
