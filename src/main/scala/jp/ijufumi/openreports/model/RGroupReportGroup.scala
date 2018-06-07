package jp.ijufumi.openreports.model

import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyNoIdCRUDMapper

case class RGroupReportGroup(groupId: Long, reportGroupId: Long)

object RGroupReportGroup extends SkinnyNoIdCRUDMapper[RGroupReportGroup] {
  override def tableName = "r_group_report_group"

  override def defaultAlias = createAlias("grp_rep_grp")

  override def extract(rs: WrappedResultSet, n: ResultName[RGroupReportGroup]): RGroupReportGroup = new RGroupReportGroup(
    groupId = rs.get(n.groupId),
    reportGroupId = rs.get(n.reportGroupId)
  )
}

