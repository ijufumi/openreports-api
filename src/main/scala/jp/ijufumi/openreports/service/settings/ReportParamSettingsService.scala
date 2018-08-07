package jp.ijufumi.openreports.service.settings

import jp.ijufumi.openreports.model.{TReportParamConfig, TReportTemplate}
import jp.ijufumi.openreports.vo.{ReportParamConfigInfo, ReportTemplateInfo}
import skinny.Logging

class ReportParamSettingsService extends Logging {
  def getReportParamConfig(): Seq[ReportParamConfigInfo] = {
    TReportParamConfig
      .findAll()
      .map(p => ReportParamConfigInfo(p.paramId, p.pageNo, p.seq))
  }
}
