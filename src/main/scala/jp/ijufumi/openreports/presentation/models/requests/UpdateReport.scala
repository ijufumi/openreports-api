package jp.ijufumi.openreports.presentation.models.requests

case class UpdateReport(name: String, templateId: String, dataSourceId: Option[String])
