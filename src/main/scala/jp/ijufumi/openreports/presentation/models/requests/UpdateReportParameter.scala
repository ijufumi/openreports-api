package jp.ijufumi.openreports.presentation.models.requests

case class UpdateReportParameter(
    parameterType: ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionType],
    value: Option[String],
)
