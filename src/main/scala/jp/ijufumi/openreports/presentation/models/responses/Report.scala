package jp.ijufumi.openreports.presentation.models.responses

case class Report(
    id: String,
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    createdAt: Long,
    updatedAt: Long,
    reportTemplate: Option[Template] = None,
)
