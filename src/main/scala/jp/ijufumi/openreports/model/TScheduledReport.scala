package jp.ijufumi.openreports.model

import org.joda.time.DateTime
import scalikejdbc.WrappedResultSet
import skinny.orm.SkinnyCRUDMapper

case class TScheduledReport(scheduleId: Long, reportId: Long, cronExpression: String, createdAt: DateTime, updatedAt: DateTime)

object TScheduledReport extends SkinnyCRUDMapper[TScheduledReport] {
  override def tableName = "t_scheduled_report"

  override def defaultAlias = createAlias("sch_rep")

  override def primaryKeyFieldName = "schedule_id"

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[TScheduledReport]): TScheduledReport = new TScheduledReport(
    scheduleId = rs.get(n.scheduleId),
    reportId = rs.get(n.reportId),
    cronExpression = rs.get(n.cronExpression),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt)
  )
}