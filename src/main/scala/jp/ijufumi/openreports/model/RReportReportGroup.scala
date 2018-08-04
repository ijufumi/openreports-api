package jp.ijufumi.openreports.model

import skinny.SkinnyJoinTable

case class RReportReportGroup(reportId: Long, reportGroupId: Long)

object RReportReportGroup extends SkinnyJoinTable[RReportReportGroup] {
  override def tableName = "r_report_report_group"

  override def defaultAlias = createAlias("rep_rep_grp")
}
