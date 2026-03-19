package jp.ijufumi.openreports.usecase.port.input.param

case class CreateReportInput(
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    parameterIds: Seq[String] = Seq.empty,
)

case class UpdateReportInput(name: String, templateId: String, dataSourceId: Option[String])

case class CreateTemplateInput(name: String)

case class UpdateTemplateInput(name: String)

case class CreateReportGroupInput(name: String, reportIds: Seq[String])

case class UpdateReportGroupInput(name: String, reportIds: Seq[String])
