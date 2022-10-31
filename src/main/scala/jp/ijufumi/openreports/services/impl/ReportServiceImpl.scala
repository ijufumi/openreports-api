package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.repositories.db.{ReportRepository, ReportTemplateRepository}
import jp.ijufumi.openreports.services.{OutputService, ReportService}
import jp.ijufumi.openreports.vo.response.{Lists, Report, ReportTemplate}

import java.io.File

class ReportServiceImpl @Inject() (
    reportRepository: ReportRepository,
    reportTemplateRepository: ReportTemplateRepository,
    outputService: OutputService,
) extends ReportService {
  def getReports(workspaceId: String, page: Int, limit: Int): Lists = {
    val offset = List(page * limit, 0).max
    val (results, count) = reportRepository.getsWithTemplate(workspaceId, offset, limit)
    val items = results.map(r => Report(r._1, r._2))
    Lists(items, offset, limit, count)
  }

  override def getReport(workspaceId: String, id: String): Option[Report] = {
    val result = reportRepository.getByIdWithTemplate(workspaceId, id)
    if (result.isEmpty) {
      return None
    }

    Some(Report(result.get._1, result.get._2))
  }

  override def getReportTemplates(workspaceId: String, page: Int, limit: Int): Lists = {
    val offset = List(page * limit, 0).max
    val (results, count) = reportTemplateRepository.gets(workspaceId, offset, limit)
    val items = results.map(r => ReportTemplate(r))
    Lists(items, offset, limit, count)
  }

  override def getReportTemplate(workspaceId: String, id: String): Option[ReportTemplate] = {
    val result = reportTemplateRepository.getById(workspaceId, id)
    if (result.isEmpty) {
      return None
    }
    Some(ReportTemplate(result.get))
  }

  override def outputReport(workspaceId: String, id: String): Option[File] = {
    val result = reportRepository.getByIdWithTemplate(workspaceId, id)
    if (result.isEmpty) {
      return None
    }
    val (report, template) = result.get
    outputService.output(workspaceId, template.filePath, template.storageType, report.dataSourceId)
  }

  override def updateReport(
      workspaceId: String,
      id: String,
      name: String,
      reportTemplateId: String,
  ): Option[Report] = {
    val reportOpt = reportRepository.getById(workspaceId, id)
    if (reportOpt.isEmpty) {
      return None
    }
    val report = reportOpt.get
    val newReport = report.copy(name = name, reportTemplateId = reportTemplateId)
    reportRepository.update(newReport)
    this.getReport(workspaceId, id)
  }
}
