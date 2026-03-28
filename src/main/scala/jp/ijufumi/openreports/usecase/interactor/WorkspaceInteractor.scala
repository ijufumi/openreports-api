package jp.ijufumi.openreports.usecase.interactor

import com.google.inject.Inject
import jp.ijufumi.openreports.domain.port.AppConfigPort
import jp.ijufumi.openreports.domain.models.value.enums.{RoleTypes, StorageTypes}
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.domain.repository.{
  ReportRepository,
  ReportTemplateRepository,
  RoleRepository,
  StorageS3Repository,
  WorkspaceMemberRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.usecase.port.input.param.{
  CreateWorkspaceMemberInput,
  UpdateWorkspaceInput,
  UpdateWorkspaceMemberInput,
}
import jp.ijufumi.openreports.domain.models.entity.{
  Lists,
  Report => ReportModel,
  ReportTemplate => ReportTemplateModel,
  StorageS3 => StorageS3Model,
  Workspace => WorkspaceModel,
  WorkspaceMember => WorkspaceMemberModel,
}
import jp.ijufumi.openreports.usecase.port.input.{StorageUseCase, WorkspaceUseCase}
import jp.ijufumi.openreports.utils.{IDs, Strings}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

class WorkspaceInteractor @Inject() (
    db: Database,
    workspaceRepository: WorkspaceRepository,
    workspaceMemberRepository: WorkspaceMemberRepository,
    storageRepository: StorageS3Repository,
    reportRepository: ReportRepository,
    reportTemplateRepository: ReportTemplateRepository,
    storageService: StorageUseCase,
    repository: RoleRepository,
    appConfig: AppConfigPort,
) extends WorkspaceUseCase {
  override def createAndRelevant(name: String, memberId: String): Option[WorkspaceModel] = {
    try {
      val workspace = WorkspaceModel(IDs.ulid(), name, Strings.generateSlug())
      workspaceRepository.register(db, workspace)
      val permission = repository.getByType(db, RoleTypes.Admin)
      if (permission.isEmpty) {
        throw new NotFoundException("permission not found")
      }
      val workspaceMember = WorkspaceMemberModel(workspace.id, memberId, permission.get.id)
      workspaceMemberRepository.register(db, workspaceMember)
      val storage = StorageS3Model(IDs.ulid(), workspace.id)
      storageRepository.register(db, storage)
      val key = this.copySample(workspace.id)
      val reportTemplate =
        ReportTemplateModel(IDs.ulid(), "copy of sample", key, workspace.id, StorageTypes.Local, 1)
      reportTemplateRepository.register(db, reportTemplate)
      val report = ReportModel(IDs.ulid(), "copy of sample", reportTemplate.id, null, workspace.id)
      reportRepository.register(db, report)

      this.getWorkspace(workspace.id)
    } catch {
      case e: Throwable =>
        SimpleDBIO(_.connection.rollback()).withPinnedSession
        throw e
    }
  }

  override def getWorkspace(id: String): Option[WorkspaceModel] = {
    workspaceRepository.getById(db, id)
  }

  override def getWorkspacesByMemberId(memberId: String): Lists[WorkspaceModel] = {
    val workspaces = workspaceRepository.getsByMemberId(db, memberId)
    Lists(
      workspaces,
      0,
      workspaces.size,
      workspaces.size,
    )
  }

  override def updateWorkspace(id: String, input: UpdateWorkspaceInput): Option[WorkspaceModel] = {
    val workspaceOpt = workspaceRepository.getById(db, id)
    if (workspaceOpt.isEmpty) {
      return None
    }
    val newWorkspace = workspaceOpt.get.copy(name = input.name)
    workspaceRepository.update(db, newWorkspace)
    this.getWorkspace(id)
  }

  override def getWorkspaceMembers(id: String): Lists[WorkspaceMemberModel] = {
    val results = workspaceMemberRepository.getsWithMember(db, id)
    Lists(
      results,
      0,
      results.size,
      results.size,
    )
  }

  override def getWorkspaceMember(
      workspaceId: String,
      memberId: String,
  ): Option[WorkspaceMemberModel] = {
    workspaceMemberRepository.getByIdWithMember(db, workspaceId, memberId)
  }

  override def createWorkspaceMember(
      workspaceId: String,
      input: CreateWorkspaceMemberInput,
  ): Option[WorkspaceMemberModel] = {
    val workspaceMember = WorkspaceMemberModel(
      workspaceId,
      input.memberId,
      input.roleId,
    )
    workspaceMemberRepository.register(db, workspaceMember)
    this.getWorkspaceMember(workspaceId, input.memberId)
  }

  override def updateWorkspaceMember(
      workspaceId: String,
      memberId: String,
      input: UpdateWorkspaceMemberInput,
  ): Option[WorkspaceMemberModel] = {
    val workspaceMemberOpt = workspaceMemberRepository.getById(db, workspaceId, memberId)
    if (workspaceMemberOpt.isEmpty) {
      return None
    }
    val newWorkspaceMember = workspaceMemberOpt.get.copy(roleId = input.roleId)
    workspaceMemberRepository.update(db, newWorkspaceMember)
    this.getWorkspaceMember(workspaceId, memberId)
  }

  override def deleteWorkspaceMember(workspaceId: String, memberId: String): Unit = {
    workspaceMemberRepository.delete(db, workspaceId, memberId)
  }

  private def copySample(workspaceId: String): String = {
    val source = storageService.get("", appConfig.sampleReportPath, StorageTypes.Local)
    val key = Strings.generateRandomString(10)() + Strings.extension(source.getFileName.toString)
    storageService.create(workspaceId, key, StorageTypes.Local, source)
    key
  }
}
