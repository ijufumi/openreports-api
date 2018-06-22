package jp.ijufumi.openreports.vo

case class ReportGroupInfo(reportGroupId: Long, reportGroupName: String) {
  def getReportGroupId: Long = {
    reportGroupId
  }

  def getReportGroupName: String = {
    reportGroupName
  }
}