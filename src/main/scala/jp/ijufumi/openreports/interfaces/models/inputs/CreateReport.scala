package jp.ijufumi.openreports.interfaces.models.inputs

case class CreateReport(
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    parameterIds: Seq[String] = Seq.empty,
)
