package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{ReportGroupReport => ReportGroupReportModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{ReportGroupReport => ReportGroupReportEntity}

object ReportGroupReportConverter {
  def toDomain(entity: ReportGroupReportEntity): ReportGroupReportModel = {
    ReportGroupReportModel(
      entity.id,
      entity.reportId,
      entity.reportGroupId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toEntity(model: ReportGroupReportModel): ReportGroupReportEntity = {
    ReportGroupReportEntity(
      model.id,
      model.reportId,
      model.reportGroupId,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def fromReportGroupReportEntity(entity: ReportGroupReportEntity): ReportGroupReportModel = toDomain(entity)
    implicit def fromReportGroupReportEntity2(entity: Option[ReportGroupReportEntity]): Option[ReportGroupReportModel] = entity.map(toDomain)
    implicit def fromReportGroupReportEntities(entity: Seq[ReportGroupReportEntity]): Seq[ReportGroupReportModel] = entity.map(toDomain)
    implicit def toReportGroupReportEntity(model: ReportGroupReportModel): ReportGroupReportEntity = toEntity(model)
    implicit def toReportGroupReportEntities(model: Seq[ReportGroupReportModel]): Seq[ReportGroupReportEntity] = model.map(toEntity)
  }
}
