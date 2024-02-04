package jp.ijufumi.openreports.models.inputs

import jp.ijufumi.openreports.models.value.enums.EmbeddedFunctionTypes.EmbeddedFunctionType
import jp.ijufumi.openreports.models.value.enums.ParameterTypes.ParameterType

case class CreateReportParameter(
    parameterType: ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionType],
    value: Option[String],
)
