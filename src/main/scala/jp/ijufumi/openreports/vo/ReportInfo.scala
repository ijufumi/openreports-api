package jp.ijufumi.openreports.vo

case class ReportInfo(_reportId: Long, _reportName: String) {
  def reportId: Long = _reportId

  def reportName = _reportName
}
