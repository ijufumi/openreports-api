package jp.ijufumi.openreports.service.settings

import java.sql.SQLException

import jp.ijufumi.openreports.model.{TReport, TReportParamConfig, TReportTemplate}
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.vo.{ReportInfo, ReportParamConfig, ReportParamConfigInfo, ReportTemplateInfo}
import org.joda.time.DateTime
import scalikejdbc.interpolation.SQLSyntax
import skinny.Logging

class ReportSettingsService extends Logging {
  def getReports: Seq[ReportInfo] = {
    TReport
      .findAll()
      .map(r => {
        val template = TReportTemplate.findById(r.templateId)
        ReportInfo(r.reportId, r.reportName, r.description, r.templateId, r.createdAt, r.updatedAt, r.versions,
          ReportTemplateInfo(r.templateId, template.get.fileName))
      })
  }

  def getReport(reportId: Long): Option[ReportInfo] = {
    TReport
      .findById(reportId)
      .map(r => {
        val template = TReportTemplate.findById(r.templateId)
        ReportInfo(r.reportId, r.reportName, r.description, r.templateId, r.createdAt, r.updatedAt, r.versions,
          ReportTemplateInfo(r.templateId, template.get.fileName))
      })
  }

  def getReportParamConfig: Seq[ReportParamConfigInfo] = {
    TReportParamConfig
      .findAll()
      .map(p => ReportParamConfigInfo(p.paramId, p.pageNo, p.seq))
  }

  def registerReport(
      reportName: String,
      description: String,
      templateId: Long
  ): StatusCode.Value = {
    try {
      TReport.createWithAttributes(
        'reportName -> reportName,
        'description -> description,
        'templateId -> templateId
      )

    } catch {
      case e: SQLException => return StatusCode.of(e)
      case _: Throwable    => return StatusCode.OTHER_ERROR
    }
    StatusCode.OK
  }

  def updateReport(
      reportId: Long,
      reportName: String,
      description: String,
      templateId: Long,
      versions: Long
  ): StatusCode.Value = {
    try {
      val reportOpt = TReport.findById(reportId)
      if (reportOpt.isEmpty) {
        return StatusCode.DATA_NOT_FOUND
      }

      val count = TReport
        .updateByIdAndVersion(reportId, versions)
        .withAttributes(
          'reportName -> reportName,
          'description -> description,
          'templateId -> templateId,
          'updatedAt -> DateTime.now()
        )
      if (count != 1) {
        return StatusCode.ALREADY_UPDATED
      }
    } catch {
      case e: SQLException => {
        logger.error("update report error", e)
        return StatusCode.of(e)}
      case e: Throwable    => {
        logger.error("update report error", e)
        return StatusCode.OTHER_ERROR
      }
    }
    StatusCode.OK
  }

  def updateReportParam(
    reportId: Long,
    params: List[ReportParamConfig],
    versions: Long
  ): StatusCode.Value = {
    try {
      val reportOpt = TReport.findById(reportId)
      if (reportOpt.isEmpty) {
        return StatusCode.DATA_NOT_FOUND
      }

      val count = TReport
        .updateByIdAndVersion(reportId, versions)
        .withAttributes(
          'updatedAt -> DateTime.now()
        )

        TReportParamConfig.deleteBy(SQLSyntax.eq(TReportParamConfig.column.column("reportId"), reportId))

        // TODO:insert
        //params.foreach()
      if (count != 1) {
        return StatusCode.ALREADY_UPDATED
      }
    } catch {
      case e: SQLException => {
        logger.error("update report error", e)
        return StatusCode.of(e)}
      case e: Throwable    => {
        logger.error("update report error", e)
        return StatusCode.OTHER_ERROR
      }
    }
    StatusCode.OK
  }

}
