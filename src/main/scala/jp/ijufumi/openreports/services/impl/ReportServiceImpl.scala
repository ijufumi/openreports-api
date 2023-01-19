package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.Template
import jp.ijufumi.openreports.entities.enums.StorageTypes
import jp.ijufumi.openreports.gateways.datastores.database.repositories.{
  ReportRepository,
  TemplateRepository,
}
import jp.ijufumi.openreports.services.{OutputService, ReportService, StorageService}
import jp.ijufumi.openreports.utils.{IDs, Strings, TemporaryFiles}
import jp.ijufumi.openreports.models.inputs.{CreateTemplate, UpdateReport, UpdateTemplate}
import jp.ijufumi.openreports.models.outputs.{Lists, Report, Template => TemplateResponse}
import org.scalatra.servlet.FileItem

import java.io.File
import scala.util.Using

class ReportServiceImpl @Inject() (
    reportRepository: ReportRepository,
    templateRepository: TemplateRepository,
    outputService: OutputService,
    storageService: StorageService,
) extends ReportService {
  def getReports(workspaceId: String, page: Int, limit: Int, templateId: String = ""): Lists[Report] = {
    val offset = List(page * limit, 0).max
    val (results, count) = reportRepository.getsWithTemplate(workspaceId, offset, limit, templateId)
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

  override def getTemplates(workspaceId: String, page: Int, limit: Int): Lists[Report] = {
    val offset = List(page * limit, 0).max
    val (results, count) = templateRepository.gets(workspaceId, offset, limit)
    val items = results.map(r => TemplateResponse(r))
    Lists(items, offset, limit, count)
  }

  override def getTemplate(workspaceId: String, id: String): Option[TemplateResponse] = {
    val result = templateRepository.getById(workspaceId, id)
    if (result.isEmpty) {
      return None
    }
    Some(TemplateResponse(result.get))
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
      input: UpdateReport,
  ): Option[Report] = {
    val reportOpt = reportRepository.getById(workspaceId, id)
    if (reportOpt.isEmpty) {
      return None
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
  ): Option[TemplateResponse] = {
    val key = Strings.generateRandomSting(10)()
    val storageType = StorageTypes.Local
    Using(TemporaryFiles.createDir()) { tmpDir =>
      val tmpFile = TemporaryFiles.createFile(tmpDir.path())
      tmpFile.write(fileItem.get())
      storageService.create(workspaceId, key, storageType, tmpFile.path())
    }
    val template = Template(IDs.ulid(), req.name, key, workspaceId, storageType, fileItem.size)
    templateRepository.register(template)
    Some(TemplateResponse.apply(template))
  }

  override def updateTemplate(
      workspaceId: String,
      id: String,
      input: UpdateTemplate,
  ): Option[TemplateResponse] = {
    val templateOpt = templateRepository.getById(workspaceId, id)
    if (templateOpt.isEmpty) {
      return None
    }
    val template = templateOpt.get.copyForUpdate(input)
    templateRepository.update(template)
    Some(TemplateResponse.apply(template))
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
}
