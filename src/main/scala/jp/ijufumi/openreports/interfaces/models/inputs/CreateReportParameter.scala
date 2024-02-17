package jp.ijufumi.openreports.interfaces.models.inputs

import jp.ijufumi.openreports.interfaces.models.value.enums.EmbeddedFunctionTypes.EmbeddedFunctionType
import jp.ijufumi.openreports.interfaces.models.value.enums.ParameterTypes.ParameterType

case class CreateReportParameter(
    parameterType: ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionType],
    value: Option[String],
)
