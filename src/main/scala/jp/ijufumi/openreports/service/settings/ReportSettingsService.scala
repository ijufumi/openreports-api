package jp.ijufumi.openreports.service.settings

import java.sql.SQLException

import jp.ijufumi.openreports.model.{TReport, TReportParamConfig}
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.vo.{ReportInfo, ReportParamConfigInfo}
import org.joda.time.DateTime
import skinny.Logging

class ReportSettingsService extends Logging {
  def getReports: Seq[ReportInfo] = {
    TReport
      .findAll()
      .map(r => ReportInfo(r.reportId, r.reportName, r.description))
  }

  def getReport(reportId: Long): Option[ReportInfo] = {
    TReport
      .findById(reportId)
      .map(r => ReportInfo(r.reportId, r.reportName, r.description))
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
      case _               => return StatusCode.OTHER_ERROR
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
          'updateAt -> DateTime.now(),
          'versions -> versions
        )
      if (count != 1) {
        return StatusCode.ALREADY_UPDATED
      }
    } catch {
      case e: SQLException => return StatusCode.of(e)
      case _               => return StatusCode.OTHER_ERROR
    }
    StatusCode.OK
  }
}
