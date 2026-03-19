package jp.ijufumi.openreports.usecase.interactor

import util.control.Breaks._
import com.google.inject.Inject
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.domain.repository.{
  ReportGroupReportRepository,
  ReportGroupRepository,
  ReportRepository,
  ReportTemplateRepository,
}
import jp.ijufumi.openreports.usecase.port.input.param.{
  CreateReportInput,
  CreateReportGroupInput,
  CreateTemplateInput,
  UpdateReportInput,
  UpdateReportGroupInput,
  UpdateTemplateInput,
}
import jp.ijufumi.openreports.domain.models.entity.{
  Lists,
  Report => ReportModel,
  ReportGroup => ReportGroupModel,
  ReportGroupReport => ReportGroupReportModel,
  ReportTemplate => ReportTemplateModel,
}
import jp.ijufumi.openreports.usecase.port.input.{
  DataSourceUseCase,
  OutputUseCase,
  ReportUseCase,
  StorageUseCase,
}
import jp.ijufumi.openreports.utils.{IDs, Strings, TemporaryFiles}
import org.scalatra.servlet.FileItem
import slick.jdbc.JdbcBackend.Database

import java.io.File
import scala.util.Using

class ReportInteractor @Inject() (
    db: Database,
    reportRepository: ReportRepository,
    templateRepository: ReportTemplateRepository,
    reportGroupRepository: ReportGroupRepository,
    reportGroupReportRepository: ReportGroupReportRepository,
    dataSourceService: DataSourceUseCase,
    outputService: OutputUseCase,
    storageService: StorageUseCase,
) extends ReportUseCase {
  def getReports(
      workspaceId: String,
      page: Int,
      limit: Int,
      templateId: String = "",
  ): Lists[ReportModel] = {
    val offset = List(page * limit, 0).max
    val (results, count) =
      reportRepository.getsWithTemplate(db, workspaceId, offset, limit, templateId)
    Lists(results, offset, limit, count)
  }

  override def getReport(workspaceId: String, id: String): Option[ReportModel] = {
    reportRepository.getByIdWithTemplate(db, workspaceId, id)
  }

  override def getTemplates(workspaceId: String, page: Int, limit: Int): Lists[ReportTemplateModel] = {
    val offset = List(page * limit, 0).max
    val (results, count) = templateRepository.gets(db, workspaceId, offset, limit)
    Lists(results, offset, limit, count)
  }

  override def getTemplate(workspaceId: String, id: String): Option[ReportTemplateModel] = {
    templateRepository.getById(db, workspaceId, id)
  }

  override def outputReport(workspaceId: String, id: String, asPDF: Boolean): Option[File] = {
    val result = reportRepository.getByIdWithTemplate(db, workspaceId, id)
    if (result.isEmpty) {
      return None
    }
    val report = result.get
    val template = report.reportTemplate.get
    outputService.output(
      workspaceId,
      template.filePath,
      template.storageType,
      report.dataSourceId,
      asPDF,
    )
  }

  override def createReport(workspaceId: String, input: CreateReportInput): Option[ReportModel] = {
    if (input.dataSourceId.isDefined) {
      val dataSourceOpt = dataSourceService.getDataSource(workspaceId, input.dataSourceId.get)
      if (dataSourceOpt.isEmpty) {
        return None
      }
    }
    val report =
      ReportModel(IDs.ulid(), input.name, input.templateId, input.dataSourceId, workspaceId)
    reportRepository.register(db, report)
    this.getReport(workspaceId, report.id)
  }

  override def updateReport(
      workspaceId: String,
      id: String,
      input: UpdateReportInput,
  ): Option[ReportModel] = {
    val reportOpt = reportRepository.getById(db, workspaceId, id)
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
    val newReport = report.copy(name = input.name, templateId = input.templateId, dataSourceId = input.dataSourceId)
    reportRepository.update(db, newReport)
    this.getReport(workspaceId, id)
  }

  override def deleteReport(workspaceId: String, id: String): Unit = {
    reportRepository.delete(db, workspaceId, id)
  }

  override def createTemplate(
      workspaceId: String,
      req: CreateTemplateInput,
      fileItem: FileItem,
  ): Option[ReportTemplateModel] = {
    val key = Strings.generateRandomSting(10)()
    val storageType = StorageTypes.Local
    Using(TemporaryFiles.createDir()) { tmpDir =>
      val tmpFile = TemporaryFiles.createFile(tmpDir.path())
      tmpFile.write(fileItem.get())
      storageService.create(workspaceId, key, storageType, tmpFile.path())
    }
    val template =
      ReportTemplateModel(IDs.ulid(), req.name, key, workspaceId, storageType, fileItem.size)
    templateRepository.register(db, template)
    this.getTemplate(workspaceId, template.id)
  }

  override def updateTemplate(
      workspaceId: String,
      id: String,
      input: UpdateTemplateInput,
  ): Option[ReportTemplateModel] = {
    val templateOpt = templateRepository.getById(db, workspaceId, id)
    if (templateOpt.isEmpty) {
      return None
    }
    val template = templateOpt.get.copy(name = input.name)
    templateRepository.update(db, template)
    this.getTemplate(workspaceId, id)
  }

  override def deleteTemplate(workspaceId: String, id: String): Unit = {
    val templateOpt = templateRepository.getById(db, workspaceId, id)
    if (templateOpt.isEmpty) {
      return
    }
    val template = templateOpt.get
    templateRepository.delete(db, workspaceId, id)
    storageService.delete(workspaceId, template.filePath, template.storageType)
  }

  override def getGroups(workspaceId: String, page: Int, limit: Int): Lists[ReportGroupModel] = {
    val offset = List(page * limit, 0).max
    val (results, count) = reportGroupRepository.gets(db, workspaceId, offset, limit)
    Lists(results, offset, limit, count)
  }

  override def getGroup(workspaceId: String, id: String): Option[ReportGroupModel] = {
    reportGroupRepository.getById(db, workspaceId, id)
  }

  override def createGroup(
      workspaceId: String,
      input: CreateReportGroupInput,
  ): Option[ReportGroupModel] = {
    val reportGroup = ReportGroupModel(IDs.ulid(), input.name, workspaceId)
    reportGroupRepository.register(db, reportGroup)
    for (reportId <- input.reportIds) {
      breakable {
        val reportOpt = reportRepository.getById(db, workspaceId, reportId)
        if (reportOpt.isEmpty) {
          break
        }
        val reportGroupReport = ReportGroupReportModel(IDs.ulid(), reportId, reportGroup.id)
        reportGroupReportRepository.register(db, reportGroupReport)
      }
    }
    getGroup(workspaceId, reportGroup.id)
  }

  override def updateGroup(
      workspaceId: String,
      id: String,
      input: UpdateReportGroupInput,
  ): Option[ReportGroupModel] = {
    val reportGroupOpt = reportGroupRepository.getById(db, workspaceId, id)
    if (reportGroupOpt.isEmpty) {
      return None
    }
    val newReportGroup = reportGroupOpt.get.copy(name = input.name)
    reportGroupRepository.update(db, newReportGroup)
    reportGroupReportRepository.deleteByReportGroupId(db, id)
    for (reportId <- input.reportIds) {
      breakable {
        val reportOpt = reportRepository.getById(db, workspaceId, reportId)
        if (reportOpt.isEmpty) {
          break
        }
        val reportGroupReport = ReportGroupReportModel(IDs.ulid(), reportId, id)
        reportGroupReportRepository.register(db, reportGroupReport)
      }
    }

    getGroup(workspaceId, id)
  }

  override def deleteGroup(workspaceId: String, id: String): Unit = {
    reportGroupReportRepository.deleteByReportGroupId(db, id)
    reportGroupRepository.delete(db, workspaceId, id)
  }
}
