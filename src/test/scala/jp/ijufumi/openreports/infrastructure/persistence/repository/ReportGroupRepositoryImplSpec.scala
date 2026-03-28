package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.ReportGroup
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.utils.{Dates, IDs}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database

class ReportGroupRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new ReportGroupRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("reportgroup_test")
    H2DatabaseHelper.createSchema(db, reportGroupQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, reportGroupQuery)
  }

  // Helper method to create a test report group
  def createTestReportGroup(
      workspaceId: String,
      name: String = "Test Group",
  ): ReportGroup = {
    ReportGroup(
      id = IDs.ulid(),
      name = name,
      workspaceId = workspaceId,
      createdAt = Dates.currentTimestamp(),
      updatedAt = Dates.currentTimestamp(),
    )
  }

  "ReportGroupRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new ReportGroupRepositoryImpl()
  }

  "register" should "create new report group and return it" in {
    val workspaceId = IDs.ulid()
    val group = createTestReportGroup(workspaceId)

    val result = repository.register(db, group)

    result should be(defined)
    result.get.id should equal(group.id)
    result.get.name should equal(group.name)
    result.get.workspaceId should equal(workspaceId)
  }

  it should "persist report group to database" in {
    val workspaceId = IDs.ulid()
    val group = createTestReportGroup(workspaceId)

    repository.register(db, group)

    val retrieved = repository.getById(db, workspaceId, group.id)
    retrieved should be(defined)
    retrieved.get.name should equal(group.name)
  }

  "getById" should "return report group when exists" in {
    val workspaceId = IDs.ulid()
    val group = createTestReportGroup(workspaceId)
    repository.register(db, group)

    val result = repository.getById(db, workspaceId, group.id)

    result should be(defined)
    result.get.id should equal(group.id)
    result.get.workspaceId should equal(workspaceId)
  }

  it should "return None when report group doesn't exist" in {
    val workspaceId = IDs.ulid()

    val result = repository.getById(db, workspaceId, "non-existent-id")

    result should be(None)
  }

  it should "filter by workspace ID" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()

    val group1 = createTestReportGroup(workspaceId1)
    val group2 = createTestReportGroup(workspaceId2)

    repository.register(db, group1)
    repository.register(db, group2)

    val result = repository.getById(db, workspaceId1, group1.id)

    result should be(defined)
    result.get.workspaceId should equal(workspaceId1)
  }

  "gets" should "return report groups for workspace with count" in {
    val workspaceId = IDs.ulid()

    val group1 = createTestReportGroup(workspaceId, "Group 1")
    val group2 = createTestReportGroup(workspaceId, "Group 2")
    val group3 = createTestReportGroup(workspaceId, "Group 3")

    repository.register(db, group1)
    repository.register(db, group2)
    repository.register(db, group3)

    val (groups, count) = repository.gets(db, workspaceId, offset = 0, limit = 10)

    groups should have size 3
    count should equal(3)
    groups.map(_.name) should contain allOf ("Group 1", "Group 2", "Group 3")
  }

  it should "respect pagination offset and limit" in {
    val workspaceId = IDs.ulid()

    // Create 10 groups
    (1 to 10).foreach { i =>
      val group = createTestReportGroup(workspaceId, s"Group $i")
      repository.register(db, group)
    }

    val (groups, count) = repository.gets(db, workspaceId, offset = 3, limit = 5)

    groups should have size 5
    count should equal(10)
  }

  it should "return all groups when limit is -1" in {
    val workspaceId = IDs.ulid()

    (1 to 15).foreach { i =>
      val group = createTestReportGroup(workspaceId, s"Group $i")
      repository.register(db, group)
    }

    val (groups, count) = repository.gets(db, workspaceId, offset = 0, limit = -1)

    groups should have size 15
    count should equal(15)
  }

  it should "return all groups when limit is 0" in {
    val workspaceId = IDs.ulid()

    (1 to 12).foreach { i =>
      val group = createTestReportGroup(workspaceId, s"Group $i")
      repository.register(db, group)
    }

    val (groups, count) = repository.gets(db, workspaceId, offset = 0, limit = 0)

    groups should have size 12
    count should equal(12)
  }

  it should "handle offset beyond results" in {
    val workspaceId = IDs.ulid()

    val group = createTestReportGroup(workspaceId)
    repository.register(db, group)

    val (groups, count) = repository.gets(db, workspaceId, offset = 10, limit = 10)

    groups should be(empty)
    count should equal(1)
  }

  it should "return empty sequence when no groups exist" in {
    val workspaceId = IDs.ulid()

    val (groups, count) = repository.gets(db, workspaceId, offset = 0, limit = 10)

    groups should be(empty)
    count should equal(0)
  }

  it should "filter by workspace ID" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()

    val group1 = createTestReportGroup(workspaceId1, "Workspace 1 Group")
    val group2 = createTestReportGroup(workspaceId2, "Workspace 2 Group")

    repository.register(db, group1)
    repository.register(db, group2)

    val (groups, count) = repository.gets(db, workspaceId1, offset = 0, limit = 10)

    groups should have size 1
    count should equal(1)
    groups.head.workspaceId should equal(workspaceId1)
    groups.head.name should equal("Workspace 1 Group")
  }

  "update" should "update existing report group" in {
    val workspaceId = IDs.ulid()
    val group = createTestReportGroup(workspaceId, "Original Name")
    repository.register(db, group)

    Thread.sleep(1000) // Ensure timestamp difference
    val updatedGroup = group.copy(name = "Updated Name")
    repository.update(db, updatedGroup)

    val result = repository.getById(db, workspaceId, group.id)
    result should be(defined)
    result.get.name should equal("Updated Name")
    result.get.updatedAt should be > group.updatedAt
  }

  it should "update timestamp when updating" in {
    val workspaceId = IDs.ulid()
    val group = createTestReportGroup(workspaceId)
    repository.register(db, group)

    val originalUpdatedAt = group.updatedAt
    Thread.sleep(10) // Ensure time passes

    repository.update(db, group)

    val result = repository.getById(db, workspaceId, group.id)
    result should be(defined)
    result.get.updatedAt should be > originalUpdatedAt
  }

  "delete" should "remove report group from database" in {
    val workspaceId = IDs.ulid()
    val group = createTestReportGroup(workspaceId)
    repository.register(db, group)

    repository.delete(db, workspaceId, group.id)

    val result = repository.getById(db, workspaceId, group.id)
    result should be(None)
  }

  it should "only delete groups for specified workspace" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()

    val group1 = createTestReportGroup(workspaceId1)
    val group2 = createTestReportGroup(workspaceId2)

    repository.register(db, group1)
    repository.register(db, group2)

    repository.delete(db, workspaceId1, group1.id)

    val (groups1, _) = repository.gets(db, workspaceId1, 0, 10)
    val (groups2, _) = repository.gets(db, workspaceId2, 0, 10)

    groups1 should be(empty)
    groups2 should have size 1
  }

  "ReportGroupRepository" should "handle special characters in name" in {
    val workspaceId = IDs.ulid()
    val group = createTestReportGroup(workspaceId).copy(
      name = "Group's \"Special\" Name! @#$%",
    )

    val result = repository.register(db, group)

    result should be(defined)
    result.get.name should equal("Group's \"Special\" Name! @#$%")
  }

  it should "handle unicode characters in name" in {
    val workspaceId = IDs.ulid()
    val group = createTestReportGroup(workspaceId).copy(
      name = "レポートグループ 日本語",
    )

    val result = repository.register(db, group)

    result should be(defined)
    result.get.name should equal("レポートグループ 日本語")
  }

  it should "handle very long names" in {
    val workspaceId = IDs.ulid()
    val longName = "A" * 200
    val group = createTestReportGroup(workspaceId).copy(name = longName)

    val result = repository.register(db, group)

    result should be(defined)
    result.get.name should have length 200
  }

  it should "handle empty name" in {
    val workspaceId = IDs.ulid()
    val group = createTestReportGroup(workspaceId).copy(name = "")

    val result = repository.register(db, group)

    result should be(defined)
    result.get.name should equal("")
  }

  it should "preserve timestamps" in {
    val workspaceId = IDs.ulid()
    val createdAt = System.currentTimeMillis()
    val group = createTestReportGroup(workspaceId).copy(
      createdAt = createdAt,
      updatedAt = createdAt,
    )

    repository.register(db, group)

    val result = repository.getById(db, workspaceId, group.id)
    result should be(defined)
    result.get.createdAt should equal(createdAt)
  }
}
