package jp.ijufumi.openreports.service.settings
import jp.ijufumi.openreports.model.TReport
import jp.ijufumi.openreports.vo.ReportInfo
import skinny.Logging

class ReportSettingsService extends Logging {
  def getReports(): Seq[ReportInfo] = {
    TReport.findAll().map(r => ReportInfo(r.reportId, r.reportName))
  }
}
