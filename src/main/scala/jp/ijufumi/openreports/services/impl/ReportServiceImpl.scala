package jp.ijufumi.openreports.services.impl

import util.control.Breaks._
import com.google.inject.Inject
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.{
  ReportGroupReportRepository,
  ReportGroupRepository,
  ReportRepository,
  TemplateRepository,
}
import jp.ijufumi.openreports.presentation.models.requests.{
  CreateReport,
  CreateReportGroup,
  CreateTemplate,
  UpdateReport,
  UpdateReportGroup,
  UpdateTemplate,
}
import jp.ijufumi.openreports.presentation.models.responses.{Lists, Report, ReportGroup, Template}
import jp.ijufumi.openreports.services.{
  DataSourceService,
  OutputService,
  ReportService,
  StorageService,
}
import jp.ijufumi.openreports.domain.models.entity.{
  Report => ReportModel,
  ReportGroup => ReportGroupModel,
  ReportGroupReport => ReportGroupReportModel,
  Template => TemplateModel,
}
import jp.ijufumi.openreports.domain.models.entity.ReportGroup.conversions._
import jp.ijufumi.openreports.domain.models.entity.Report.conversions._
import jp.ijufumi.openreports.utils.{IDs, Strings, TemporaryFiles}
import org.scalatra.servlet.FileItem

import java.io.File
import scala.util.Using

class ReportServiceImpl @Inject() (
    reportRepository: ReportRepository,
    templateRepository: TemplateRepository,
    reportGroupRepository: ReportGroupRepository,
    reportGroupReportRepository: ReportGroupReportRepository,
    dataSourceService: DataSourceService,
    outputService: OutputService,
    storageService: StorageService,
) extends ReportService {
  def getReports(
      workspaceId: String,
      page: Int,
      limit: Int,
      templateId: String = "",
  ): Lists[Report] = {
    val offset = List(page * limit, 0).max
    val (results, count) = reportRepository.getsWithTemplate(workspaceId, offset, limit, templateId)
    Lists(results, offset, limit, count)
  }

  override def getReport(workspaceId: String, id: String): Option[Report] = {
    reportRepository.getByIdWithTemplate(workspaceId, id)
  }

  override def getTemplates(workspaceId: String, page: Int, limit: Int): Lists[Template] = {
    val offset = List(page * limit, 0).max
    val (results, count) = templateRepository.gets(workspaceId, offset, limit)
    Lists(results, offset, limit, count)
  }

  override def getTemplate(workspaceId: String, id: String): Option[Template] = {
    templateRepository.getById(workspaceId, id).map(t => t.toResponse)
  }

  override def outputReport(workspaceId: String, id: String): Option[File] = {
    val result = reportRepository.getByIdWithTemplate(workspaceId, id)
    if (result.isEmpty) {
      return None
    }
    val report = result.get
    val template = report.reportTemplate.get
    outputService.output(workspaceId, template.filePath, template.storageType, report.dataSourceId)
  }

  override def createReport(workspaceId: String, input: CreateReport): Option[Report] = {
    if (input.dataSourceId.isDefined) {
      val dataSourceOpt = dataSourceService.getDataSource(workspaceId, input.dataSourceId.get)
      if (dataSourceOpt.isEmpty) {
        return None
      }
    }
    val report =
      ReportModel(IDs.ulid(), input.name, input.templateId, input.dataSourceId, workspaceId)
    reportRepository.register(report)
    this.getReport(workspaceId, report.id)
  }

  override def updateReport(
      workspaceId: String,
      id: String,
      input: UpdateReport,
  ): Option[Report] = {
    val reportOpt = reportRepository.getById(workspaceId, id)
    if (reportOpt.isEmpty) {
      return None
    }
    if (input.dataSourceId.isDefined) {
      val dataSourceOpt = dataSourceService.getDataSource(workspaceId, input.dataSourceId.get)
      if (dataSourceOpt.isEmpty) {
        return None
      }
    }
    val report = reportOpt.get
    val newReport = report.copyForUpdate(input)
    reportRepository.update(newReport)
    this.getReport(workspaceId, id)
  }

  override def deleteReport(workspaceId: String, id: String): Unit = {
    reportRepository.delete(workspaceId, id)
  }

  override def createTemplate(
      workspaceId: String,
      req: CreateTemplate,
      fileItem: FileItem,
  ): Option[Template] = {
    val key = Strings.generateRandomSting(10)()
    val storageType = StorageTypes.Local
    Using(TemporaryFiles.createDir()) { tmpDir =>
      val tmpFile = TemporaryFiles.createFile(tmpDir.path())
      tmpFile.write(fileItem.get())
      storageService.create(workspaceId, key, storageType, tmpFile.path())
    }
    val template = TemplateModel(IDs.ulid(), req.name, key, workspaceId, storageType, fileItem.size)
    templateRepository.register(template)
    this.getTemplate(workspaceId, template.id)
  }

  override def updateTemplate(
      workspaceId: String,
      id: String,
      input: UpdateTemplate,
  ): Option[Template] = {
    val templateOpt = templateRepository.getById(workspaceId, id)
    if (templateOpt.isEmpty) {
      return None
    }
    val template = templateOpt.get.copyForUpdate(input)
    templateRepository.update(template)
    this.getTemplate(workspaceId, id)
  }

  override def deleteTemplate(workspaceId: String, id: String): Unit = {
    val templateOpt = templateRepository.getById(workspaceId, id)
    if (templateOpt.isEmpty) {
      return
    }
    val template = templateOpt.get
    templateRepository.delete(workspaceId, id)
    storageService.delete(workspaceId, template.filePath, template.storageType)
  }

  override def getGroups(workspaceId: String, page: Int, limit: Int): Lists[ReportGroup] = {
    val offset = List(page * limit, 0).max
    val (results, count) = reportGroupRepository.gets(workspaceId, offset, limit)
    Lists(results, offset, limit, count)
  }

  override def getGroup(workspaceId: String, id: String): Option[ReportGroup] = {
    reportGroupRepository.getById(workspaceId, id)
  }

  override def createGroup(
      workspaceId: String,
      input: CreateReportGroup,
  ): Option[ReportGroup] = {
    val reportGroup = ReportGroupModel(IDs.ulid(), input.name, workspaceId)
    reportGroupRepository.register(reportGroup)
    for (reportId <- input.reportIds) {
      breakable {
        val reportOpt = reportRepository.getById(workspaceId, reportId)
        if (reportOpt.isEmpty) {
          break
        }
        val reportGroupReport = ReportGroupReportModel(IDs.ulid(), reportId, reportGroup.id)
        reportGroupReportRepository.register(reportGroupReport)
      }
    }
    getGroup(workspaceId, reportGroup.id)
  }

  override def updateGroup(
      workspaceId: String,
      id: String,
      input: UpdateReportGroup,
  ): Option[ReportGroup] = {
    val reportGroupOpt = reportGroupRepository.getById(workspaceId, id)
    if (reportGroupOpt.isEmpty) {
      return None
    }
    val newReportGroup = reportGroupOpt.get.copyForUpdate(input)
    reportGroupRepository.update(newReportGroup)
    reportGroupReportRepository.deleteByReportGroupId(id)
    for (reportId <- input.reportIds) {
      breakable {
        val reportOpt = reportRepository.getById(workspaceId, reportId)
        if (reportOpt.isEmpty) {
          break
        }
        val reportGroupReport = ReportGroupReportModel(IDs.ulid(), reportId, id)
        reportGroupReportRepository.register(reportGroupReport)
      }
    }

    getGroup(workspaceId, id)
  }

  override def deleteGroup(workspaceId: String, id: String): Unit = {
    reportGroupReportRepository.deleteByReportGroupId(id)
    reportGroupRepository.delete(workspaceId, id)
  }
}
