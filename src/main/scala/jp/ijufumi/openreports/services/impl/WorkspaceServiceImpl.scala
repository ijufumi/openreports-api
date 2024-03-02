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
import jp.ijufumi.openreports.domain.models.entity.{
  Report => ReportModel,
  StorageS3 => StorageS3Model,
  Template => TemplateModel,
  WorkspaceMember => WorkspaceMemberModel,
}
import jp.ijufumi.openreports.services.{StorageService, WorkspaceService}
import jp.ijufumi.openreports.utils.{IDs, Strings}
import slick.jdbc.PostgresProfile.api._

class WorkspaceServiceImpl @Inject() (
    workspaceRepository: WorkspaceRepository,
    workspaceMemberRepository: WorkspaceMemberRepository,
    storageRepository: StorageS3Repository,
    reportRepository: ReportRepository,
    reportTemplateRepository: TemplateRepository,
    storageService: StorageService,
    permissionRepository: RoleRepository,
) extends WorkspaceService {
  override def createAndRelevant(
      input: CreateWorkspace,
      memberId: String,
  ): Option[Workspace] = {
    createAndRelevant(input.name, memberId)
  }

  override def createAndRelevant(name: String, memberId: String): Option[Workspace] = {
    var workspaceOpt = Option.empty[Workspace]
    try {
      val workspace = Workspace(IDs.ulid(), name, Strings.generateSlug())
      workspaceOpt = Some(workspace)
      workspaceRepository.register(workspace)
      val permission = permissionRepository.getByType(RoleTypes.Admin)
      if (permission.isEmpty) {
        throw new NotFoundException("permission not found")
      }
      val workspaceMember = WorkspaceMemberModel(workspace.id, memberId, permission.get.id)
      workspaceMemberRepository.register(workspaceMember)
      val storage = StorageS3Model(IDs.ulid(), workspace.id)
      storageRepository.register(storage)
      val key = this.copySample(workspace.id)
      val reportTemplate =
        TemplateModel(IDs.ulid(), "copy of sample", key, workspace.id, StorageTypes.Local, 1)
      reportTemplateRepository.register(reportTemplate)
      val report = ReportModel(IDs.ulid(), "copy of sample", reportTemplate.id, null, workspace.id)
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

  override def getWorkspacesByMemberId(memberId: String): Lists[Workspace] = {
    val workspaces = workspaceRepository.getsByMemberId(memberId)
    Lists(
      workspaces,
      0,
      workspaces.size,
      workspaces.size,
    )
  }

  override def updateWorkspace(id: String, input: UpdateWorkspace): Option[Workspace] = {
    val workspaceOpt = workspaceRepository.getById(id)
    if (workspaceOpt.isEmpty) {
      return None
    }
    val newWorkspace = workspaceOpt.get.mergeForUpdate(input)
    workspaceRepository.update(newWorkspace)
  }

  override def getWorkspaceMembers(id: String): Lists[WorkspaceMember] = {
    val results = workspaceMemberRepository.getsWithMember(id)
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
    workspaceMemberRepository.getByIdWithMember(workspaceId, memberId).map((m => m.toResponse))
  }

  override def createWorkspaceMember(
      workspaceId: String,
      input: CreateWorkspaceMember,
  ): Option[WorkspaceMember] = {
    val workspaceMember = WorkspaceMemberModel(
      workspaceId,
      input.memberId,
      input.permissionId,
    )
    workspaceMemberRepository.register(workspaceMember)
    this.getWorkspaceMember(workspaceId, input.memberId)
  }

  override def updateWorkspaceMember(
      workspaceId: String,
      memberId: String,
      input: UpdateWorkspaceMember,
  ): Option[WorkspaceMember] = {
    val workspaceMemberOpt = workspaceMemberRepository.getById(workspaceId, memberId)
    if (workspaceMemberOpt.isEmpty) {
      return None
    }
    val newWorkspaceMember = workspaceMemberOpt.get.copyForUpdate(input)
    workspaceMemberRepository.update(newWorkspaceMember)
    this.getWorkspaceMember(workspaceId, memberId)
  }

  override def deleteWorkspaceMember(workspaceId: String, memberId: String): Unit = {
    workspaceMemberRepository.delete(workspaceId, memberId)
  }

  private def copySample(workspaceId: String): String = {
    val source = storageService.get("", Config.SAMPLE_REPORT_PATH, StorageTypes.Local)
    val key = Strings.generateRandomSting(10)() + Strings.extension(source.getFileName.toString)
    storageService.create(workspaceId, key, StorageTypes.Local, source)
    key
  }
}
