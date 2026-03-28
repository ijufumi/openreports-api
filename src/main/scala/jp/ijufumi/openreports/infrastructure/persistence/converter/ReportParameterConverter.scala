package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{ReportParameter => ReportParameterModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  ReportParameter => ReportParameterEntity,
}

object ReportParameterConverter {
  def toDomain(entity: ReportParameterEntity): ReportParameterModel = {
    ReportParameterModel(
      entity.id,
      entity.workspaceId,
      entity.parameterType,
      entity.embeddedFunctionType,
      entity.value,
      entity.createdAt,
      entity.updatedAt,
    )
  }

  def toEntity(model: ReportParameterModel): ReportParameterEntity = {
    ReportParameterEntity(
      model.id,
      model.workspaceId,
      model.parameterType,
      model.embeddedFunctionType,
      model.value,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toReportParameterEntity(model: ReportParameterModel): ReportParameterEntity =
      toEntity(model)
    implicit def fromReportParameterEntity(entity: ReportParameterEntity): ReportParameterModel =
      toDomain(entity)
    implicit def fromReportParameterEntity2(
        entity: Option[ReportParameterEntity],
    ): Option[ReportParameterModel] = entity.map(toDomain)
    implicit def fromReportParameterEntities(
        entity: Seq[ReportParameterEntity],
    ): Seq[ReportParameterModel] = entity.map(toDomain)
  }
}
