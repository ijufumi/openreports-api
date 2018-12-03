package jp.ijufumi.openreports.model

import java.time.LocalDateTime

import scalikejdbc.WrappedResultSet
import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeature

case class TScheduledReport(scheduleId: Long,
                            reportId: Long,
                            cronExpression: String,
                            createdAt: LocalDateTime,
                            updatedAt: LocalDateTime,
                            versions: Long)

object TScheduledReport
    extends SkinnyCRUDMapper[TScheduledReport]
    with OptimisticLockWithVersionFeature[TScheduledReport] {
  override def tableName = "t_scheduled_report"

  override def defaultAlias = createAlias("sch_rep")

  override def primaryKeyFieldName = "scheduleId"

  override def lockVersionFieldName: String = "versions"

  override def extract(
    rs: WrappedResultSet,
    n: scalikejdbc.ResultName[TScheduledReport]
  ): TScheduledReport = new TScheduledReport(
    scheduleId = rs.get(n.scheduleId),
    reportId = rs.get(n.reportId),
    cronExpression = rs.get(n.cronExpression),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt),
    versions = rs.get(n.versions)
  )
}
