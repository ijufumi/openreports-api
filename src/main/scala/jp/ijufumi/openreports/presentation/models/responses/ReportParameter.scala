package jp.ijufumi.openreports.presentation.models.responses

import jp.ijufumi.openreports.domain.models.value.enums.{EmbeddedFunctionTypes, ParameterTypes}
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  ReportParameter => ReportParameterEntity,
}
import jp.ijufumi.openreports.utils.Dates

case class ReportParameter(
    id: String,
    workspaceId: String,
    parameterType: ParameterTypes.ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionTypes.EmbeddedFunctionType],
    value: Option[String],
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: ReportParameterEntity = {
    ReportParameterEntity(
      this.id,
      this.workspaceId,
      this.parameterType,
      this.embeddedFunctionType,
      this.value,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
}

object ReportParameter {
  def apply(entity: ReportParameterEntity): ReportParameter = {
    ReportParameter(
      entity.id,
      entity.workspaceId,
      entity.parameterType,
      entity.embeddedFunctionType,
      entity.value,
      entity.createdAt,
      entity.updatedAt,
    )
  }
}
