package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.request.CreateTemplate
import jp.ijufumi.openreports.vo.response.{Lists, Report, Template}
import org.scalatra.servlet.FileItem

import java.io.File

trait ReportService {
  def getReports(workspaceId: String, page: Int, limit: Int): Lists

  def getTemplates(workspaceId: String, page: Int, limit: Int): Lists

  def getTemplate(workspaceId: String, id: String): Option[Template]

  def getReport(workspaceId: String, id: String): Option[Report]

  def outputReport(workspaceId: String, id: String): Option[File]

  def updateReport(workspaceId: String, id: String, name: String, reportTemplateId: String): Option[Report]

  def deleteReport(workspaceId: String, id: String): Unit

  def createTemplate(workspaceId: String, req: CreateTemplate, fileItem: FileItem): Option[Template]
}
