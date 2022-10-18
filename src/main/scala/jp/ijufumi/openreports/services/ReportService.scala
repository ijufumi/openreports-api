package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.{Lists, Report, ReportTemplate}

import java.io.File

trait ReportService {
  def getReports(page: Int, limit: Int): Lists

  def getReportTemplates(page: Int, limit: Int): Lists

  def getReportTemplate(id: String): Option[ReportTemplate]

  def getReport(id: String): Option[Report]

  def outputReport(id: String): Option[File]
}
