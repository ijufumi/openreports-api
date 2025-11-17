package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.domain.models.value.enums.{EmbeddedFunctionTypes, ParameterTypes}

case class CreateReportParameter(
    parameterType: ParameterTypes.ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionTypes.EmbeddedFunctionType],
    value: Option[String],
)
