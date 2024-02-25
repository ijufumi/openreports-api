package jp.ijufumi.openreports.presentation.models.responses

case class Report(
    id: String,
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    reportTemplate: Option[Template] = None,
)
