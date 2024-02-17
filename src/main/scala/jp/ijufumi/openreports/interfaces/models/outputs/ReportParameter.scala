package jp.ijufumi.openreports.interfaces.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{
  ReportParameter => ReportParameterEntity,
}
import jp.ijufumi.openreports.interfaces.models.value.enums.EmbeddedFunctionTypes.EmbeddedFunctionType
import jp.ijufumi.openreports.interfaces.models.value.enums.ParameterTypes.ParameterType
import jp.ijufumi.openreports.utils.Dates

case class ReportParameter(
    id: String,
    workspaceId: String,
    parameterType: ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionType],
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
