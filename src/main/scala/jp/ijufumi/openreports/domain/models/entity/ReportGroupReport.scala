package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  ReportGroupReport => ReportGroupReportEntity,
}
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

  object conversions {
    import scala.language.implicitConversions

    implicit def fromReportGroupReportEntity(entity: ReportGroupReportEntity): ReportGroupReport = {
      ReportGroupReport(entity)
    }
    implicit def fromReportGroupReportEntity2(
        entity: Option[ReportGroupReportEntity],
    ): Option[ReportGroupReport] = {
      entity.map(e => ReportGroupReport(e))
    }
    implicit def fromReportGroupReportEntities(
        entity: Seq[ReportGroupReportEntity],
    ): Seq[ReportGroupReport] = {
      entity.map(e => ReportGroupReport(e))
    }
    implicit def toReportGroupReportEntity(model: ReportGroupReport): ReportGroupReportEntity = {
      model.toEntity
    }
    implicit def toReportGroupReportEntities(
        model: Seq[ReportGroupReport],
    ): Seq[ReportGroupReportEntity] = {
      model.map(m => m.toEntity)
    }
  }
}
