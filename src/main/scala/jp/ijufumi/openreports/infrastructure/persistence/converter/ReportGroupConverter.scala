package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{ReportGroup => ReportGroupModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{ReportGroup => ReportGroupEntity}

object ReportGroupConverter {
  def toDomain(entity: ReportGroupEntity): ReportGroupModel = {
    ReportGroupModel(
      entity.id,
      entity.name,
      entity.workspaceId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toEntity(model: ReportGroupModel): ReportGroupEntity = {
    ReportGroupEntity(
      model.id,
      model.name,
      model.workspaceId,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toReportGroupEntity(model: ReportGroupModel): ReportGroupEntity = toEntity(model)
    implicit def toReportGroupEntities(model: Seq[ReportGroupModel]): Seq[ReportGroupEntity] =
      model.map(toEntity)
    implicit def fromReportGroupEntity(entity: ReportGroupEntity): ReportGroupModel = toDomain(
      entity,
    )
    implicit def fromReportGroupEntity2(
        entity: Option[ReportGroupEntity],
    ): Option[ReportGroupModel] = entity.map(toDomain)
    implicit def fromReportGroupEntities(entity: Seq[ReportGroupEntity]): Seq[ReportGroupModel] =
      entity.map(toDomain)
  }
}
