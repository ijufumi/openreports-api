package jp.ijufumi.openreports.model

import skinny.SkinnyJoinTable

case class RGroupReportGroup(groupId: Long, reportGroupId: Long)

object RGroupReportGroup extends SkinnyJoinTable[RGroupReportGroup] {
  override def tableName = "r_group_report_group"

  override def defaultAlias = createAlias("grp_rep_grp")
}
