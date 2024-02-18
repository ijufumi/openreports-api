package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{ReportGroupReport => ReportGroupReportEntity}
import jp.ijufumi.openreports.utils.Dates

case class ReportGroupReport(
    id: String,
    reportId: String,
    reportGroupId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: ReportGroupReportEntity = {
    ReportGroupReportEntity(
      this.id,
      this.reportId,
      this.reportGroupId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
}

object ReportGroupReport {
  def apply(entity: ReportGroupReportEntity): ReportGroupReport = {
    ReportGroupReport(
      entity.id,
      entity.reportId,
      entity.reportGroupId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
}
