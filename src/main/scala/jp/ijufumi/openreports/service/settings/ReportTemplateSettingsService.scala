package jp.ijufumi.openreports.service.settings

import jp.ijufumi.openreports.model.{TReportTemplate, TReportTemplateHistory}
import jp.ijufumi.openreports.vo.ReportTemplateInfo
import org.joda.time.DateTime
import skinny.Logging
import skinny.micro.multipart.FileItem

class ReportTemplateSettingsService extends Logging {
  def getReportTemplates(): Seq[ReportTemplateInfo] = {
    TReportTemplate
      .findAll()
      .map(r => ReportTemplateInfo(r.templateId, r.fileName))
  }

  def uploadFile(file: FileItem): Unit = {
    val templates = TReportTemplate
      .where('fileNme -> file.name)
      .apply()

    if (templates.isEmpty) {
      TReportTemplate.createWithAttributes('fileName -> file.name,
                                           'filePath -> file.name)
    } else {
      val template = templates.head

      TReportTemplate
        .updateByIdAndVersion(template.templateId, template.versions)
        .withAttributes(
          'fileName -> file.name,
          'filePath -> file.name,
          'updatedAt -> DateTime.now(),
          'versions -> (template.versions + 1)
        )

      TReportTemplateHistory
        .createWithAttributes(
          'templateId -> template.templateId,
          'fileName -> template.fileName,
          'filePath -> template.filePath,
          'createdAt -> template.createdAt,
          'updatedAt -> template.updatedAt,
          'versions -> template.versions
        )
    }
  }
}
