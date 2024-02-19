package jp.ijufumi.openreports.presentation.models.requests

case class CreateReportGroup(name: String, reportIds: Seq[String])
