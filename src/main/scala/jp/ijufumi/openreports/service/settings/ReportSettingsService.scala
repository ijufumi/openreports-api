package jp.ijufumi.openreports.service.settings
import jp.ijufumi.openreports.model.{TReport, TReportParamConfig}
import jp.ijufumi.openreports.vo.{ReportInfo, ReportParamConfigInfo}
import skinny.Logging

class ReportSettingsService extends Logging {
  def getReports: Seq[ReportInfo] = {
    TReport.findAll().map(r => ReportInfo(r.reportId, r.reportName))
  }

  def getReportParamConfig: Seq[ReportParamConfigInfo] = {
    TReportParamConfig
      .findAll()
      .map(p => ReportParamConfigInfo(p
        .paramId, p
        .pageNo, p
        .seq))
  }
}
