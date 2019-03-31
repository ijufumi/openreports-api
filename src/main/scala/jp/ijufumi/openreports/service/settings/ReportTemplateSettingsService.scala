package jp.ijufumi.openreports.service.settings

import java.nio.file.{FileSystems, Files}

import jp.ijufumi.openreports.model.{TReportTemplate, TReportTemplateHistory}
import jp.ijufumi.openreports.service.OutputFilePath
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.service.support.ConnectionFactory
import jp.ijufumi.openreports.vo.{ReportTemplateHistoryInfo, ReportTemplateInfo}
import org.joda.time.DateTime
import scalikejdbc.DB
import skinny.Logging
import skinny.micro.multipart.FileItem

class ReportTemplateSettingsService extends Logging {
  val blankTemplate = new ReportTemplateInfo(0L, "")

  def getReportTemplates: Array[ReportTemplateInfo] = {
    TReportTemplate
      .findAll()
      .map(r => ReportTemplateInfo(r.templateId, r.fileName)).toArray
  }

  def getReportTemplate(templateId: Long): ReportTemplateInfo = {
    TReportTemplate
      .findById(templateId)
      .map(r => ReportTemplateInfo(r.templateId, r.fileName)).getOrElse(blankTemplate)
  }

  def getHistories(templateId: Long): Array[ReportTemplateHistoryInfo] = {
    TReportTemplateHistory
      .where('templateId -> templateId)
      .apply()
      .sortBy(_.historyId)
      .map(r => ReportTemplateHistoryInfo(r.historyId, r.templateId, r.fileName, r.createdAt)).toArray
  }

  def uploadFile(file: FileItem): StatusCode.Value = {
    val templates = TReportTemplate.where('fileName -> file.name).apply()

    val filePath = "%s_%s".format(DateTime.now().toString("yyyyMMddHHmmss"), file.name)

    val db = DB(ConnectionFactory.getConnection)
    try {
      db.begin()
      if (templates.isEmpty) {
        TReportTemplate
          .createWithAttributes('fileName -> file.name, 'filePath -> filePath)
      } else {
        val template = templates.head

        TReportTemplate
          .updateByIdAndVersion(template.templateId, template.versions)
          .withAttributes(
            'fileName -> file.name,
            'filePath -> filePath,
            'updatedAt -> DateTime
              .now()
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

      val fullPath = FileSystems.getDefault.getPath(OutputFilePath, filePath)
      if (!Files.exists(fullPath.getParent)) {
        Files.createDirectories(fullPath.getParent)
      }
      logger.debug("filePath:%s".format(fullPath.toString))
      file.write(fullPath.toFile)
      StatusCode.OK
    } catch {
      case e: Throwable => {
        db.rollback()
        logger.error("file upload failed.", e)
        StatusCode.OTHER_ERROR
      }
    }
  }
}
