package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.{Member, WorkspaceMember}
import jp.ijufumi.openreports.exceptions.OptimisticLockException
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database

class WorkspaceMemberRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new WorkspaceMemberRepositoryImpl()
  val memberRepository = new MemberRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("workspacemember_test")
    H2DatabaseHelper.createSchema(db, workspaceMemberQuery, memberQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, workspaceMemberQuery, memberQuery)
  }

  // Helper method to create a test member
  def createTestMember(email: String = s"test${IDs.ulid()}@example.com"): Member = {
    val member = Member(
      id = IDs.ulid(),
      email = email,
      name = "Test User",
      googleId = Some(IDs.ulid()),
      password = "hashedpassword",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )
    memberRepository.register(db, member)
    member
  }

  // Helper method to create a test workspace member
  def createTestWorkspaceMember(
      workspaceId: String,
      memberId: String,
      roleId: String = "role-id",
  ): WorkspaceMember = {
    WorkspaceMember(
      workspaceId = workspaceId,
      memberId = memberId,
      roleId = roleId,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )
  }

  "WorkspaceMemberRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new WorkspaceMemberRepositoryImpl()
  }

  "register" should "create new workspace member and return it" in {
    val workspaceId = IDs.ulid()
    val member = createTestMember()
    val workspaceMember = createTestWorkspaceMember(workspaceId, member.id)

    val result = repository.register(db, workspaceMember)

    result should be(defined)
    result.get.workspaceId should equal(workspaceId)
    result.get.memberId should equal(member.id)
    result.get.roleId should equal(workspaceMember.roleId)
  }

  "getById" should "return workspace member when exists" in {
    val workspaceId = IDs.ulid()
    val member = createTestMember()
    val workspaceMember = createTestWorkspaceMember(workspaceId, member.id)
    repository.register(db, workspaceMember)

    val result = repository.getById(db, workspaceId, member.id)

    result should be(defined)
    result.get.workspaceId should equal(workspaceId)
    result.get.memberId should equal(member.id)
  }

  it should "return None when workspace member doesn't exist" in {
    val result = repository.getById(db, "workspace-id", "member-id")

    result should be(None)
  }

  it should "filter by workspace ID" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()
    val member = createTestMember()

    val wm1 = createTestWorkspaceMember(workspaceId1, member.id)
    val wm2 = createTestWorkspaceMember(workspaceId2, member.id)

    repository.register(db, wm1)
    repository.register(db, wm2)

    val result = repository.getById(db, workspaceId1, member.id)

    result should be(defined)
    result.get.workspaceId should equal(workspaceId1)
  }

  "gets" should "return all workspace members for workspace" in {
    val workspaceId = IDs.ulid()
    val member1 = createTestMember()
    val member2 = createTestMember()

    repository.register(db, createTestWorkspaceMember(workspaceId, member1.id))
    repository.register(db, createTestWorkspaceMember(workspaceId, member2.id))

    val result = repository.gets(db, workspaceId)

    result should have size 2
    result.map(_.memberId) should contain allOf (member1.id, member2.id)
  }

  it should "return empty sequence when no members exist" in {
    val result = repository.gets(db, IDs.ulid())

    result should be(empty)
  }

  "getsByMemberId" should "return all workspaces for member" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()
    val member = createTestMember()

    repository.register(db, createTestWorkspaceMember(workspaceId1, member.id))
    repository.register(db, createTestWorkspaceMember(workspaceId2, member.id))

    val result = repository.getsByMemberId(db, member.id)

    result should have size 2
    result.map(_.workspaceId) should contain allOf (workspaceId1, workspaceId2)
  }

  it should "return empty sequence when member has no workspaces" in {
    val result = repository.getsByMemberId(db, "non-existent-member")

    result should be(empty)
  }

  "getByIdWithMember" should "return workspace member with member details" in {
    val workspaceId = IDs.ulid()
    val member = createTestMember()
    repository.register(db, createTestWorkspaceMember(workspaceId, member.id))

    val result = repository.getByIdWithMember(db, workspaceId, member.id)

    result should be(defined)
    result.get.member should be(defined)
    result.get.member.get.id should equal(member.id)
    result.get.member.get.email should equal(member.email)
  }

  it should "return None when not found" in {
    val result = repository.getByIdWithMember(db, "workspace-id", "member-id")

    result should be(None)
  }

  "getsWithMember" should "return all workspace members with member details" in {
    val workspaceId = IDs.ulid()
    val member1 = createTestMember("user1@example.com")
    val member2 = createTestMember("user2@example.com")

    repository.register(db, createTestWorkspaceMember(workspaceId, member1.id))
    repository.register(db, createTestWorkspaceMember(workspaceId, member2.id))

    val result = repository.getsWithMember(db, workspaceId)

    result should have size 2
    result.foreach { wm =>
      wm.member should be(defined)
    }
    result.map(_.member.get.email) should contain allOf ("user1@example.com", "user2@example.com")
  }

  "update" should "update existing workspace member" in {
    val workspaceId = IDs.ulid()
    val member = createTestMember()
    val workspaceMember = createTestWorkspaceMember(workspaceId, member.id, "old-role")
    repository.register(db, workspaceMember)

    val updated = workspaceMember.copy(roleId = "new-role")
    repository.update(db, updated)

    val result = repository.getById(db, workspaceId, member.id)
    result should be(defined)
    result.get.roleId should equal("new-role")
  }

  it should "throw OptimisticLockException when versions do not match" in {
    val workspaceId = IDs.ulid()
    val member = createTestMember()
    val workspaceMember = createTestWorkspaceMember(workspaceId, member.id, "old-role")
    repository.register(db, workspaceMember)
    val stale = repository.getById(db, workspaceId, member.id).get

    repository.update(db, stale.copy(roleId = "first"))

    an[OptimisticLockException] should be thrownBy
      repository.update(db, stale.copy(roleId = "second"))
  }

  "delete" should "remove workspace member from database" in {
    val workspaceId = IDs.ulid()
    val member = createTestMember()
    repository.register(db, createTestWorkspaceMember(workspaceId, member.id))

    repository.delete(db, workspaceId, member.id)

    val result = repository.getById(db, workspaceId, member.id)
    result should be(None)
  }

  it should "only delete members for specified workspace" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()
    val member = createTestMember()

    repository.register(db, createTestWorkspaceMember(workspaceId1, member.id))
    repository.register(db, createTestWorkspaceMember(workspaceId2, member.id))

    repository.delete(db, workspaceId1, member.id)

    repository.getById(db, workspaceId1, member.id) should be(None)
    repository.getById(db, workspaceId2, member.id) should be(defined)
  }

  it should "only delete the specified member within a workspace" in {
    val workspaceId = IDs.ulid()
    val member1 = createTestMember()
    val member2 = createTestMember()

    repository.register(db, createTestWorkspaceMember(workspaceId, member1.id))
    repository.register(db, createTestWorkspaceMember(workspaceId, member2.id))

    repository.delete(db, workspaceId, member1.id)

    repository.getById(db, workspaceId, member1.id) should be(None)
    repository.getById(db, workspaceId, member2.id) should be(defined)
    repository.gets(db, workspaceId) should have size 1
  }

  "WorkspaceMemberRepository" should "handle multiple roles" in {
    val workspaceId = IDs.ulid()
    val member1 = createTestMember()
    val member2 = createTestMember()

    repository.register(db, createTestWorkspaceMember(workspaceId, member1.id, "admin"))
    repository.register(db, createTestWorkspaceMember(workspaceId, member2.id, "viewer"))

    val result = repository.gets(db, workspaceId)

    result should have size 2
    result.map(_.roleId) should contain allOf ("admin", "viewer")
  }
}
