package jp.ijufumi.openreports.service.settings

import jp.ijufumi.openreports.model.TReportTemplate
import jp.ijufumi.openreports.vo.{ReportInfo, ReportTemplateInfo}
import skinny.Logging

class ReportTemplateSettingsService extends Logging {
  def getReportTemplates(): Seq[ReportInfo] = {
    TReportTemplate.findAll().map(r => ReportTemplateInfo(r.templateId, r.templatePath))
  }
}
