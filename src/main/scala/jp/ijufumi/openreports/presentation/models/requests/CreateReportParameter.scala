package jp.ijufumi.openreports.presentation.models.requests

case class CreateReportParameter(
    parameterType: ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionType],
    value: Option[String],
)
