package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.usecase.port.input.param.{
  CreateReportGroupInput,
  CreateReportInput,
  CreateTemplateInput,
  UpdateReportGroupInput,
  UpdateReportInput,
  UpdateTemplateInput,
}
import jp.ijufumi.openreports.domain.models.entity.{Lists, Report, ReportGroup, ReportTemplate}
import org.scalatra.servlet.FileItem

import java.io.File

trait ReportUseCase {
  def getReports(workspaceId: String, page: Int, limit: Int, templateId: String = ""): Lists[Report]

  def getTemplates(workspaceId: String, page: Int, limit: Int): Lists[ReportTemplate]

  def getTemplate(workspaceId: String, id: String): Option[ReportTemplate]

  def getReport(workspaceId: String, id: String): Option[Report]

  def outputReport(workspaceId: String, id: String, asPDF: Boolean): Option[File]

  def createReport(workspaceId: String, input: CreateReportInput): Option[Report]

  def updateReport(workspaceId: String, id: String, input: UpdateReportInput): Option[Report]

  def deleteReport(workspaceId: String, id: String): Unit

  def createTemplate(
      workspaceId: String,
      req: CreateTemplateInput,
      fileItem: FileItem,
  ): Option[ReportTemplate]

  def updateTemplate(
      workspaceId: String,
      id: String,
      input: UpdateTemplateInput,
  ): Option[ReportTemplate]

  def deleteTemplate(workspaceId: String, id: String): Unit

  def getGroups(workspaceId: String, page: Int, limit: Int): Lists[ReportGroup]

  def getGroup(workspaceId: String, id: String): Option[ReportGroup]

  def createGroup(workspaceId: String, input: CreateReportGroupInput): Option[ReportGroup]

  def updateGroup(
      workspaceId: String,
      id: String,
      input: UpdateReportGroupInput,
  ): Option[ReportGroup]

  def deleteGroup(workspaceId: String, id: String): Unit
}
