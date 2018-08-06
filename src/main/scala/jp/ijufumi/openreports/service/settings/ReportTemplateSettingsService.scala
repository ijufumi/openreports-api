package jp.ijufumi.openreports.service.settings

import jp.ijufumi.openreports.model.TReportTemplate
import jp.ijufumi.openreports.vo.ReportTemplateInfo
import skinny.Logging

class ReportTemplateSettingsService extends Logging {
  def getReportTemplates(): Seq[ReportTemplateInfo] = {
    TReportTemplate
      .findAll()
      .map(r => ReportTemplateInfo(r.templateId, r.fileName))
  }
}
