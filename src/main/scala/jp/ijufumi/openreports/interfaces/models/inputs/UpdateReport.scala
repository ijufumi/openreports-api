package jp.ijufumi.openreports.interfaces.models.inputs

case class UpdateReport(name: String, templateId: String, dataSourceId: Option[String])
