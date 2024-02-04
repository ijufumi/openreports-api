package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{
  ReportParameter => ReportParameterEntity,
}
import jp.ijufumi.openreports.models.value.enums.EmbeddedFunctionTypes.EmbeddedFunctionType
import jp.ijufumi.openreports.models.value.enums.ParameterTypes.ParameterType

case class ReportParameter(
    id: String,
    parameterType: ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionType],
    value: Option[String],
    createdAt: Long,
    updatedAt: Long,
)

object ReportParameter {
  def apply(entity: ReportParameterEntity): ReportParameter = {
    ReportParameter(
      entity.id,
      entity.parameterType,
      entity.embeddedFunctionType,
      entity.value,
      entity.createdAt,
      entity.updatedAt,
    )
  }
}
