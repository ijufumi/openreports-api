package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.{ResultName, WrappedResultSet}
import skinny.orm.SkinnyCRUDMapper

case class TReportGroup(reportGroupId: Long, reportGroupName: String, createdAt: DateTime, updatedAt: DateTime)

object TReportGroup extends SkinnyCRUDMapper[TReportGroup] {
  override def tableName = "t_report_group"

  override def defaultAlias = createAlias("rep_grp")

  override def primaryKeyFieldName = "report_group_id"

  override def extract(rs: WrappedResultSet, n: ResultName[TReportGroup]): TReportGroup = new TReportGroup(
    reportGroupId = rs.get(n.reportGroupId),
    reportGroupName = rs.get(n.reportGroupName),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}
