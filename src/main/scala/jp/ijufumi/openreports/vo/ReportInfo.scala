package jp.ijufumi.openreports.vo

case class ReportInfo(_reportId: Long, _reportName: String, _templateFile: String = "") {
  def reportId: Long = _reportId

  def reportName: String = _reportName

  def templateFile: String = _templateFile
}
