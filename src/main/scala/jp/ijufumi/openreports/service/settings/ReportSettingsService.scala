package jp.ijufumi.openreports.service.settings

import java.sql.SQLException

import jp.ijufumi.openreports.model.{RReportReportGroup, TReport, TReportParamConfig, TReportTemplate}
import jp.ijufumi.openreports.service.enums.StatusCode
import jp.ijufumi.openreports.vo.{ReportInfo, ReportParamConfig, ReportParamConfigInfo, ReportTemplateInfo}
import org.joda.time.DateTime
import scalikejdbc.sqls
import skinny.Logging

class ReportSettingsService extends Logging {
  def getReports: Array[ReportInfo] = {
    TReport
      .findAll()
      .map(r => {
        val groups = r.groups.map(_.reportGroupId).seq
        ReportInfo(r.reportId,
                   r.reportName,
                   r.description,
                   r.templateId,
                   groups,
                   r.createdAt,
                   r.updatedAt,
                   r.versions)
      }).toArray
  }

  def getReport(reportId: Long): Option[ReportInfo] = {
    TReport
      .findById(reportId)
      .map(r => {
        val groups = r.groups.map(_.reportGroupId).seq
        ReportInfo(r.reportId,
                   r.reportName,
                   r.description,
                   r.templateId,
                   groups,
                   r.createdAt,
                   r.updatedAt,
                   r.versions)
      })
  }

  def getReportParamConfig(reportId: Long): Array[ReportParamConfigInfo] = {
    TReportParamConfig
      .findAllBy(sqls.eq(TReportParamConfig.column.field("reportId"), reportId))
      .map(p => ReportParamConfigInfo(p.paramId, p.pageNo, p.seq))
      .sortWith((x, y) => {
        if (x.pageNo == y.pageNo) {
           x.seq < y.seq
        } else {
          x.pageNo < y.pageNo
        }
      }).toArray
  }

  def registerReport(
      reportInfo: ReportInfo
  ): StatusCode.Value = {
    try {
      val reportId = TReport.createWithAttributes(
        "reportName" -> reportInfo.reportName,
        "description" -> reportInfo.description,
        "templateId" -> reportInfo.templateId,
        "createdAt" -> DateTime.now,
        "updatedAt" -> DateTime.now
      )

      reportInfo.groups.foreach(x => {
        RReportReportGroup.createWithAttributes(
          "reportId" -> reportId,
          "reportGroupId" -> x
        )
      })
    } catch {
      case e: SQLException => return StatusCode.of(e)
      case _: Throwable    => return StatusCode.OTHER_ERROR
    }
    StatusCode.OK
  }

  def updateReport(
      reportId: Long,
    reportInfo: ReportInfo
  ): StatusCode.Value = {
    try {
      val reportOpt = TReport.findById(reportId)
      if (reportOpt.isEmpty) {
        return StatusCode.DATA_NOT_FOUND
      }

      val count = TReport
        .updateByIdAndVersion(reportId, reportInfo.versions)
        .withAttributes(
          "reportName" -> reportInfo.reportName,
          "description" -> reportInfo.description,
          "templateId" -> reportInfo.templateId,
          "updatedAt" -> DateTime.now
        )
      if (count != 1) {
        return StatusCode.ALREADY_UPDATED
      }

      val condition = sqls.eq(RReportReportGroup.column.field("reportId"), reportId)
      val current = RReportReportGroup.findAllBy(condition)

      if (current.nonEmpty) {
        RReportReportGroup.deleteBy(condition)
      }

      reportInfo.groups.foreach(x => {
        RReportReportGroup.createWithAttributes(
          "reportId" -> reportId,
          "reportGroupId" -> x
        )
      })
    } catch {
      case e: SQLException => {
        logger.error("update report error", e)
        return StatusCode.of(e)
      }
      case e: Throwable => {
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
          "updatedAt" -> DateTime.now
        )

      if (count != 1) {
        return StatusCode.ALREADY_UPDATED
      }

      val condition = sqls.eq(TReportParamConfig.column.field("reportId"), reportId)
      val current = TReportParamConfig.findAllBy(condition)

      if (current.nonEmpty) {
        TReportParamConfig.deleteBy(condition)
      }
      params.foreach(
        x =>
          TReportParamConfig.createWithAttributes("reportId" -> reportId,
            "paramId" -> x.paramId,
            "pageNo" -> x.pageNo,
            "seq" -> x.seq,
            "createdAt" -> DateTime.now,
            "updatedAt" -> DateTime.now))
    } catch {
      case e: SQLException => {
        logger.error("update report error", e)
        return StatusCode.of(e)
      }
      case e: Throwable => {
        logger.error("update report error", e)
        return StatusCode.OTHER_ERROR
      }
    }
    StatusCode.OK
  }

}
