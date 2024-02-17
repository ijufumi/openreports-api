package jp.ijufumi.openreports.interfaces.models.outputs

import jp.ijufumi.openreports.utils.Dates
import jp.ijufumi.openreports.gateways.datastores.database.entities.{
  ReportGroupReport => ReportGroupReportEntity,
}

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
