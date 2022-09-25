package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.repositories.db.ReportRepository
import jp.ijufumi.openreports.services.{OutputService, ReportService}
import jp.ijufumi.openreports.vo.response.{Report, Reports}

import java.io.File

class ReportServiceImpl @Inject() (reportRepository: ReportRepository, outputService: OutputService)
    extends ReportService {
  def getReports(): Reports = {
    val results = reportRepository.getsWithTemplate
    val items = results.map(r => Report(r._1, r._2))
    Reports(items, 0, 0, items.length)
  }

  override def getReport(id: String): Option[Report] = {
    val result = reportRepository.getByIdWithTemplate(id)
    if (result.isEmpty) {
      return None
    }

    Some(Report(result.get._1, result.get._2))
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