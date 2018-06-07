package jp.ijufumi.openreports.model

import skinny.SkinnyJoinTable

case class RReportReportParam(reportId: Long, paramId: Long)

object RReportReportParam extends SkinnyJoinTable[RReportReportParam] {
  override def tableName = "r_report_report_param"

  override def defaultAlias = createAlias("rep_rep_param")
}
