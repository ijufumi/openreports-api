package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{
  ReportReportParameter => ReportReportParameterEntity,
}
import jp.ijufumi.openreports.utils.Dates

case class ReportReportParameter(
    id: String,
    reportId: String,
    reportParameterId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: ReportReportParameterEntity = {
    ReportReportParameterEntity(
      this.id,
      this.reportId,
      this.reportParameterId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
}

object ReportReportParameter {
  def apply(entity: ReportReportParameterEntity): ReportReportParameter = {
    ReportReportParameter(
      entity.id,
      entity.reportId,
      entity.reportParameterId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
}
