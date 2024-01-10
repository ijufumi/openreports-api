package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.models.inputs.{CreateReport, CreateReportGroup, CreateTemplate, UpdateReport, UpdateReportGroup, UpdateTemplate}
import jp.ijufumi.openreports.models.outputs.{Lists, Report, ReportGroup, Template}
import org.scalatra.servlet.FileItem

import java.io.File

trait ReportService {
  def getReports(workspaceId: String, page: Int, limit: Int, templateId: String = ""): Lists[Report]

  def getTemplates(workspaceId: String, page: Int, limit: Int): Lists[Template]

  def getTemplate(workspaceId: String, id: String): Option[Template]

  def getReport(workspaceId: String, id: String): Option[Report]

  def outputReport(workspaceId: String, id: String): Option[File]

  def createReport(workspaceId: String, input: CreateReport): Option[Report]

  def updateReport(workspaceId: String, id: String, input: UpdateReport): Option[Report]

  def deleteReport(workspaceId: String, id: String): Unit

  def createTemplate(workspaceId: String, req: CreateTemplate, fileItem: FileItem): Option[Template]

  def updateTemplate(workspaceId: String, id: String, input: UpdateTemplate): Option[Template]

  def deleteTemplate(workspaceId: String, id: String): Unit

  def getGroups(workspaceId: String, page: Int, limit: Int): Lists[ReportGroup]

  def getGroup(workspaceId: String, id: String): Option[ReportGroup]

  def createGroup(workspaceId: String, input: CreateReportGroup): Option[ReportGroup]

  def updateGroup(workspaceId: String, id: String, input: UpdateReportGroup): Option[ReportGroup]

  def deleteGroup(workspaceId: String, id: String): Unit
}
