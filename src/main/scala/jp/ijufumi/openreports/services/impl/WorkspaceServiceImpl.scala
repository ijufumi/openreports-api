package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.entities.enums.StorageTypes
import jp.ijufumi.openreports.entities.{Report, Storage, Template, Workspace, WorkspaceMember}
import jp.ijufumi.openreports.gateways.datastores.database.repositories.{
  ReportRepository,
  StorageRepository,
  TemplateRepository,
  WorkspaceMemberRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.models.inputs.UpdateWorkspace
import jp.ijufumi.openreports.services.{StorageService, WorkspaceService}
import jp.ijufumi.openreports.utils.{IDs, Strings}
import slick.jdbc.PostgresProfile.api._

class WorkspaceServiceImpl @Inject() (
    workspaceRepository: WorkspaceRepository,
    workspaceMemberRepository: WorkspaceMemberRepository,
    storageRepository: StorageRepository,
    reportRepository: ReportRepository,
    reportTemplateRepository: TemplateRepository,
    storageService: StorageService,
) extends WorkspaceService {
  override def createAndRelevant(memberId: String, email: String): Option[Workspace] = {
    var workspaceOpt = Option.empty[Workspace]
    try {
      val workspaceName = Strings.nameFromEmail(email) + "'s workspace"
      val workspace = Workspace(IDs.ulid(), workspaceName, Strings.generateSlug())
      workspaceOpt = Some(workspace)
      workspaceRepository.register(workspace)
      val workspaceMember = WorkspaceMember(workspace.id, memberId)
      workspaceMemberRepository.register(workspaceMember)
      val storage = Storage(IDs.ulid(), workspace.id)
      storageRepository.register(storage)
      val key = this.copySample(workspace.id)
      val reportTemplate =
        Template(IDs.ulid(), "copy of sample", key, workspace.id, StorageTypes.Local, 1)
      reportTemplateRepository.register(reportTemplate)
      val report = Report(IDs.ulid(), "copy of sample", reportTemplate.id, null, workspace.id)
      reportRepository.register(report)
    } catch {
      case e: Throwable =>
        SimpleDBIO(_.connection.rollback()).withPinnedSession
        throw e
    }

    workspaceOpt
  }

  override def getWorkspace(id: String): Option[Workspace] = {
    workspaceRepository.getById(id)
  }

  override def updateWorkspace(id: String, input: UpdateWorkspace): Option[Workspace] = {
    val workspaceOpt = workspaceRepository.getById(id)
    if (workspaceOpt.isEmpty) {
      return None
    }
    val newWorkspace = workspaceOpt.get.copy(name = input.name)
    workspaceRepository.update(newWorkspace)
  }

  private def copySample(workspaceId: String): String = {
    val source = storageService.get("", Config.SAMPLE_REPORT_PATH, StorageTypes.Local)
    val key = Strings.generateRandomSting(10)() + Strings.extension(source.getFileName.toString)
    storageService.create(workspaceId, key, StorageTypes.Local, source)
    key
  }
}
