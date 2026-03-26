package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{ReportTemplate => ReportTemplateModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  ReportTemplate => ReportTemplateEntity,
}

object ReportTemplateConverter {
  def toDomain(entity: ReportTemplateEntity): ReportTemplateModel = {
    ReportTemplateModel(
      entity.id,
      entity.name,
      entity.filePath,
      entity.workspaceId,
      entity.storageType,
      entity.fileSize,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toEntity(model: ReportTemplateModel): ReportTemplateEntity = {
    ReportTemplateEntity(
      model.id,
      model.name,
      model.filePath,
      model.workspaceId,
      model.storageType,
      model.fileSize,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toTemplateEntity(model: ReportTemplateModel): ReportTemplateEntity = toEntity(
      model,
    )
    implicit def fromTemplateEntity(entity: ReportTemplateEntity): ReportTemplateModel = toDomain(
      entity,
    )
    implicit def fromTemplateEntity2(
        entity: Option[ReportTemplateEntity],
    ): Option[ReportTemplateModel] = entity.map(toDomain)
    implicit def fromTemplateEntities(entity: Seq[ReportTemplateEntity]): Seq[ReportTemplateModel] =
      entity.map(toDomain)
  }
}
