package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.response.{Lists, Report, ReportTemplate}

import java.io.File

trait ReportService {
  def getReports(workspaceId: String, page: Int, limit: Int): Lists

  def getReportTemplates(workspaceId: String, page: Int, limit: Int): Lists

  def getReportTemplate(workspaceId: String, id: String): Option[ReportTemplate]

  def getReport(workspaceId: String, id: String): Option[Report]

  def outputReport(workspaceId: String, id: String): Option[File]

  def updateReport(workspaceId: String, id: String, name: String, reportTemplateId: String): Option[Report]

  def deleteReport(workspaceId: String, id: String): Unit
}
