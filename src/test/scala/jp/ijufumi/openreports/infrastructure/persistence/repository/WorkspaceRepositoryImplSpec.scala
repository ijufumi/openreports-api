package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.infrastructure.persistence.entity.{Workspace, WorkspaceMember}
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

class WorkspaceRepositoryImplSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  var db: Database = _
  val repository = new WorkspaceRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("workspace_test")
    H2DatabaseHelper.createSchema(db, workspaceQuery, workspaceMemberQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, workspaceMemberQuery, workspaceQuery)
  }

  "WorkspaceRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new WorkspaceRepositoryImpl()
  }

  "getById" should "return workspace when exists" in {
    val workspaceId = IDs.ulid()

    val workspace = Workspace(
      id = workspaceId,
      name = "Test Workspace",
      slug = "test-workspace",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, workspace)

    val result = repository.getById(db, workspaceId)

    result should be(defined)
    result.get.id should equal(workspaceId)
    result.get.name should equal("Test Workspace")
    result.get.slug should equal("test-workspace")
  }

  it should "return None when workspace doesn't exist" in {
    val result = repository.getById(db, "non-existent-id")

    result should be(None)
  }

  "getsByMemberId" should "return workspaces for member" in {
    val memberId = IDs.ulid()
    val workspace1Id = IDs.ulid()
    val workspace2Id = IDs.ulid()

    val workspace1 = Workspace(
      id = workspace1Id,
      name = "Workspace 1",
      slug = "workspace-1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val workspace2 = Workspace(
      id = workspace2Id,
      name = "Workspace 2",
      slug = "workspace-2",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, workspace1)
    repository.register(db, workspace2)

    // Create workspace member associations
    val workspaceMember1 = WorkspaceMember(
      workspaceId = workspace1Id,
      memberId = memberId,
      roleId = "role-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val workspaceMember2 = WorkspaceMember(
      workspaceId = workspace2Id,
      memberId = memberId,
      roleId = "role-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    Await.result(db.run(workspaceMemberQuery += workspaceMember1), 5.seconds)
    Await.result(db.run(workspaceMemberQuery += workspaceMember2), 5.seconds)

    val result = repository.getsByMemberId(db, memberId)

    result should not be empty
    result.length should be(2)
    result.exists(_.id == workspace1Id) should be(true)
    result.exists(_.id == workspace2Id) should be(true)
  }

  it should "return empty sequence when member has no workspaces" in {
    val result = repository.getsByMemberId(db, "non-existent-member-id")

    result should be(empty)
  }

  "register" should "create new workspace and return it" in {
    val workspaceId = IDs.ulid()

    val workspace = Workspace(
      id = workspaceId,
      name = "New Workspace",
      slug = "new-workspace",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val result = repository.register(db, workspace)

    result should be(defined)
    result.get.id should equal(workspace.id)
    result.get.name should equal(workspace.name)
  }

  "update" should "update existing workspace" in {
    val workspaceId = IDs.ulid()

    val workspace = Workspace(
      id = workspaceId,
      name = "Old Name",
      slug = "old-slug",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, workspace)

    val updatedWorkspace = workspace.copy(
      name = "Updated Name",
      slug = "updated-slug",
      updatedAt = System.currentTimeMillis()
    )

    val result = repository.update(db, updatedWorkspace)

    result should be(defined)
    result.get.name should equal("Updated Name")
    result.get.slug should equal("updated-slug")
  }
}
