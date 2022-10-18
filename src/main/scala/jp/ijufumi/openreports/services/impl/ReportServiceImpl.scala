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
  def getReports(page: Int, limit: Int): Lists = {
    val offset = List(page * limit, 0).max
    val (results, count) = reportRepository.getsWithTemplate(offset, limit)
    val items = results.map(r => Report(r._1, r._2))
    Lists(items, offset, limit, count)
  }

  override def getReport(id: String): Option[Report] = {
    val result = reportRepository.getByIdWithTemplate(id)
    if (result.isEmpty) {
      return None
    }

    Some(Report(result.get._1, result.get._2))
  }

  override def getReportTemplates(page: Int, limit: Int): Lists = {
    val offset = List(page * limit, 0).max
    val (results, count) = reportTemplateRepository.gets(offset, limit)
    val items = results.map(r => ReportTemplate(r))
    Lists(items, offset, limit, count)
  }

  override def getReportTemplate(id: String): Option[ReportTemplate] = {
    val result = reportTemplateRepository.getById(id)
    if (result.isEmpty) {
      return None
    }
    Some(ReportTemplate(result.get))
  }

  override def outputReport(id: String): Option[File] = {
    val result = reportRepository.getByIdWithTemplate(id)
    if (result.isEmpty) {
      return None
    }
    val (report, template) = result.get
    outputService.output(template.filePath, report.dataSourceId)
  }
}
