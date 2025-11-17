package jp.ijufumi.openreports.presentation.response

case class Report(
                   id: String,
                   name: String,
                   reportTemplateId: String,
                   dataSourceId: Option[String],
                   createdAt: Long,
                   updatedAt: Long,
                   reportTemplate: Option[ReportTemplate] = None,
)
