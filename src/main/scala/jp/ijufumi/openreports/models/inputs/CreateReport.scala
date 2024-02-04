package jp.ijufumi.openreports.models.inputs

case class CreateReport(
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    parameterIds: Seq[String] = Seq.empty,
)
