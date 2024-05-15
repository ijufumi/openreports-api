package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.domain.models.value.enums.{RoleTypes, StorageTypes}
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.{
  ReportRepository,
  RoleRepository,
  StorageS3Repository,
  TemplateRepository,
  WorkspaceMemberRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.presentation.models.requests.{
  CreateWorkspace,
  CreateWorkspaceMember,
  UpdateWorkspace,
  UpdateWorkspaceMember,
}
import jp.ijufumi.openreports.presentation.models.responses.{Lists, Workspace, WorkspaceMember}
import jp.ijufumi.openreports.domain.models.entity.WorkspaceMember.conversions._
import jp.ijufumi.openreports.domain.models.entity.Workspace.conversions._
import jp.ijufumi.openreports.domain.models.entity.{
  Report => ReportModel,
  StorageS3 => StorageS3Model,
  Template => TemplateModel,
  Workspace => WorkspaceModel,
  WorkspaceMember => WorkspaceMemberModel,
}
import jp.ijufumi.openreports.services.{StorageService, WorkspaceService}
import jp.ijufumi.openreports.utils.{IDs, Strings}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

class WorkspaceServiceImpl @Inject() (
    db: Database,
    workspaceRepository: WorkspaceRepository,
    workspaceMemberRepository: WorkspaceMemberRepository,
    storageRepository: StorageS3Repository,
    reportRepository: ReportRepository,
    reportTemplateRepository: TemplateRepository,
    storageService: StorageService,
    repository: RoleRepository,
) extends WorkspaceService {
  override def createAndRelevant(
      input: CreateWorkspace,
      memberId: String,
  ): Option[Workspace] = {
    createAndRelevant(input.name, memberId)
  }

  override def createAndRelevant(name: String, memberId: String): Option[Workspace] = {
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
        TemplateModel(IDs.ulid(), "copy of sample", key, workspace.id, StorageTypes.Local, 1)
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

  override def getWorkspace(id: String): Option[Workspace] = {
    workspaceRepository.getById(db, id)
  }

  override def getWorkspacesByMemberId(memberId: String): Lists[Workspace] = {
    val workspaces = workspaceRepository.getsByMemberId(db, memberId)
    Lists(
      workspaces,
      0,
      workspaces.size,
      workspaces.size,
    )
  }

  override def updateWorkspace(id: String, input: UpdateWorkspace): Option[Workspace] = {
    val workspaceOpt = workspaceRepository.getById(db, id)
    if (workspaceOpt.isEmpty) {
      return None
    }
    val newWorkspace = workspaceOpt.get.mergeForUpdate(input)
    workspaceRepository.update(db, newWorkspace)
    this.getWorkspace(id)
  }

  override def getWorkspaceMembers(id: String): Lists[WorkspaceMember] = {
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
  ): Option[WorkspaceMember] = {
    workspaceMemberRepository.getByIdWithMember(db, workspaceId, memberId)
  }

  override def createWorkspaceMember(
      workspaceId: String,
      input: CreateWorkspaceMember,
  ): Option[WorkspaceMember] = {
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
      input: UpdateWorkspaceMember,
  ): Option[WorkspaceMember] = {
    val workspaceMemberOpt = workspaceMemberRepository.getById(db, workspaceId, memberId)
    if (workspaceMemberOpt.isEmpty) {
      return None
    }
    val newWorkspaceMember = workspaceMemberOpt.get.copyForUpdate(input)
    workspaceMemberRepository.update(db, newWorkspaceMember)
    this.getWorkspaceMember(workspaceId, memberId)
  }

  override def deleteWorkspaceMember(workspaceId: String, memberId: String): Unit = {
    workspaceMemberRepository.delete(db, workspaceId, memberId)
  }

  private def copySample(workspaceId: String): String = {
    val source = storageService.get("", Config.SAMPLE_REPORT_PATH, StorageTypes.Local)
    val key = Strings.generateRandomSting(10)() + Strings.extension(source.getFileName.toString)
    storageService.create(workspaceId, key, StorageTypes.Local, source)
    key
  }
}
