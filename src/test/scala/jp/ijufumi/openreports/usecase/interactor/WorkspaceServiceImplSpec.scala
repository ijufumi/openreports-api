package jp.ijufumi.openreports.usecase.interactor

import _root_.jp.ijufumi.openreports.domain.models.entity.{Lists, Workspace => WorkspaceModel}
import _root_.jp.ijufumi.openreports.domain.port.AppConfigPort
import _root_.jp.ijufumi.openreports.domain.repository._
import _root_.jp.ijufumi.openreports.usecase.port.input.StorageUseCase
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class WorkspaceInteractorSpec extends AnyFlatSpec with MockitoSugar {

  "getWorkspace" should "return workspace" in {
    // mock
    val db = mock[Database]
    val workspaceRepository = mock[WorkspaceRepository]
    val workspaceMemberRepository = mock[WorkspaceMemberRepository]
    val storageRepository = mock[StorageS3Repository]
    val reportRepository = mock[ReportRepository]
    val reportTemplateRepository = mock[ReportTemplateRepository]
    val storageService = mock[StorageUseCase]
    val roleRepository = mock[RoleRepository]
    val appConfig = mock[AppConfigPort]

    val workspaceService = new WorkspaceInteractor(
      db,
      workspaceRepository,
      workspaceMemberRepository,
      storageRepository,
      reportRepository,
      reportTemplateRepository,
      storageService,
      roleRepository,
      appConfig,
    )

    val workspaceId = "1"
    val workspace = WorkspaceModel(workspaceId, "test", "test-slug")

    when(workspaceRepository.getById(db, workspaceId)).thenReturn(Some(workspace))

    // when
    val actual = workspaceService.getWorkspace(workspaceId)

    // then
    assert(actual.isDefined)
    assert(actual.get.name == "test")
  }

  "getWorkspacesByMemberId" should "return workspaces" in {
    // mock
    val db = mock[Database]
    val workspaceRepository = mock[WorkspaceRepository]
    val workspaceMemberRepository = mock[WorkspaceMemberRepository]
    val storageRepository = mock[StorageS3Repository]
    val reportRepository = mock[ReportRepository]
    val reportTemplateRepository = mock[ReportTemplateRepository]
    val storageService = mock[StorageUseCase]
    val roleRepository = mock[RoleRepository]
    val appConfig = mock[AppConfigPort]

    val workspaceService = new WorkspaceInteractor(
      db,
      workspaceRepository,
      workspaceMemberRepository,
      storageRepository,
      reportRepository,
      reportTemplateRepository,
      storageService,
      roleRepository,
      appConfig,
    )

    val memberId = "1"
    val workspace = WorkspaceModel("1", "test", "test-slug")

    when(workspaceRepository.getsByMemberId(db, memberId)).thenReturn(Seq(workspace))

    // when
    val actual = workspaceService.getWorkspacesByMemberId(memberId)

    // then
    assert(actual.isInstanceOf[Lists[WorkspaceModel]])
    assert(actual.items.length == 1)
    assert(actual.count == 1)
    assert(actual.items.head.name == "test")
  }
}
