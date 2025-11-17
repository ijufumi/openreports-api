package jp.ijufumi.openreports.presentation.request

case class UpdateReport(name: String, templateId: String, dataSourceId: Option[String])
