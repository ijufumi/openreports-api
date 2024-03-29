package jp.ijufumi.openreports.presentation.models.requests

case class CreateReport(
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    parameterIds: Seq[String] = Seq.empty,
)
