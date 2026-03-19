package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{Report => ReportModel, ReportTemplate => ReportTemplateModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Report => ReportEntity, ReportTemplate => ReportTemplateEntity}

object ReportConverter {
  def toDomain(entity: ReportEntity): ReportModel = {
    ReportModel(
      entity.id,
      entity.name,
      entity.reportTemplateId,
      entity.dataSourceId,
      entity.workspaceId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toDomain(entity: (ReportEntity, ReportTemplateEntity)): ReportModel = {
    ReportModel(
      entity._1.id,
      entity._1.name,
      entity._1.reportTemplateId,
      entity._1.dataSourceId,
      entity._1.workspaceId,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      Some(ReportTemplateConverter.toDomain(entity._2)),
    )
  }

  def toEntity(model: ReportModel): ReportEntity = {
    ReportEntity(
      model.id,
      model.name,
      model.templateId,
      model.dataSourceId,
      model.workspaceId,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toReportEntity(model: ReportModel): ReportEntity = toEntity(model)
    implicit def fromReportEntity(entity: ReportEntity): ReportModel = toDomain(entity)
    implicit def fromReportEntity2(entity: Option[ReportEntity]): Option[ReportModel] = entity.map(toDomain)
    implicit def fromReportEntity3(entity: (ReportEntity, ReportTemplateEntity)): ReportModel = toDomain(entity)
    implicit def fromReportEntity4(entity: Option[(ReportEntity, ReportTemplateEntity)]): Option[ReportModel] = entity.map(toDomain)
    implicit def fromReportEntities(entity: Seq[ReportEntity]): Seq[ReportModel] = entity.map(toDomain)
    implicit def fromReportEntities2(entity: Seq[(ReportEntity, ReportTemplateEntity)]): Seq[ReportModel] = entity.map(toDomain)
  }
}
