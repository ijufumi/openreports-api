package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.WrappedResultSet
import skinny.orm.SkinnyCRUDMapper

case class ScheduledReport(scheduleId: Long, reportId: Long, cronExpression: String, createdAt: DateTime, updatedAt: DateTime)

object ScheduledReport extends SkinnyCRUDMapper[ScheduledReport] {
  override def tableName = "scheduled_report"
  override def defaultAlias = createAlias("sch_rep")
  override def primaryKeyFieldName = "schedule_id"

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[ScheduledReport]): ScheduledReport = new ScheduledReport(
    scheduleId = rs.get(n.scheduleId),
    reportId = rs.get(n.reportId),
    cronExpression = rs.get(n.cronExpression),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}