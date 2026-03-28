package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.Role
import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Role => RoleEntity}
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

class RoleRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new RoleRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("role_test")
    H2DatabaseHelper.createSchema(db, roleQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, roleQuery)
  }

  // Helper method to insert test role directly into DB
  def insertRole(
      id: String = IDs.ulid(),
      roleType: RoleTypes.RoleType,
      createdAt: Long = System.currentTimeMillis(),
      updatedAt: Long = System.currentTimeMillis(),
      versions: Long = 1,
  ): RoleEntity = {
    val entity = RoleEntity(id, roleType, createdAt, updatedAt, versions)
    Await.result(db.run(roleQuery += entity), 10.seconds)
    entity
  }

  "RoleRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new RoleRepositoryImpl()
  }

  "getAll" should "return all roles" in {
    insertRole(roleType = RoleTypes.Admin)
    insertRole(roleType = RoleTypes.Developer)
    insertRole(roleType = RoleTypes.Viewer)

    val result = repository.getAll(db)

    result should have size 3
    result.map(
      _.roleType,
    ) should contain allOf (RoleTypes.Admin, RoleTypes.Developer, RoleTypes.Viewer)
  }

  it should "return empty sequence when no roles exist" in {
    val result = repository.getAll(db)

    result should be(empty)
  }

  it should "return roles with correct IDs" in {
    val adminId = IDs.ulid()
    val developerId = IDs.ulid()

    insertRole(id = adminId, roleType = RoleTypes.Admin)
    insertRole(id = developerId, roleType = RoleTypes.Developer)

    val result = repository.getAll(db)

    result.map(_.id) should contain allOf (adminId, developerId)
  }

  it should "handle multiple calls consistently" in {
    insertRole(roleType = RoleTypes.Admin)
    insertRole(roleType = RoleTypes.Viewer)

    val result1 = repository.getAll(db)
    val result2 = repository.getAll(db)

    result1 should have size 2
    result2 should have size 2
    result1.map(_.id) should equal(result2.map(_.id))
  }

  "getByType" should "return role when it exists" in {
    val adminId = IDs.ulid()
    insertRole(id = adminId, roleType = RoleTypes.Admin)
    insertRole(roleType = RoleTypes.Developer)

    val result = repository.getByType(db, RoleTypes.Admin)

    result should be(defined)
    result.get.id should equal(adminId)
    result.get.roleType should equal(RoleTypes.Admin)
  }

  it should "return None when role type doesn't exist" in {
    insertRole(roleType = RoleTypes.Developer)

    val result = repository.getByType(db, RoleTypes.Admin)

    result should be(None)
  }

  it should "return correct role for each role type" in {
    val adminId = IDs.ulid()
    val devId = IDs.ulid()
    val viewerId = IDs.ulid()

    insertRole(id = adminId, roleType = RoleTypes.Admin)
    insertRole(id = devId, roleType = RoleTypes.Developer)
    insertRole(id = viewerId, roleType = RoleTypes.Viewer)

    val admin = repository.getByType(db, RoleTypes.Admin)
    val developer = repository.getByType(db, RoleTypes.Developer)
    val viewer = repository.getByType(db, RoleTypes.Viewer)

    admin should be(defined)
    admin.get.id should equal(adminId)

    developer should be(defined)
    developer.get.id should equal(devId)

    viewer should be(defined)
    viewer.get.id should equal(viewerId)
  }

  "Role conversion" should "correctly convert from entity to domain model" in {
    val id = IDs.ulid()
    val createdAt = System.currentTimeMillis()
    val updatedAt = createdAt + 1000

    insertRole(
      id = id,
      roleType = RoleTypes.Admin,
      createdAt = createdAt,
      updatedAt = updatedAt,
      versions = 2,
    )

    val result = repository.getByType(db, RoleTypes.Admin)

    result should be(defined)
    result.get.id should equal(id)
    result.get.roleType should equal(RoleTypes.Admin)
    result.get.createdAt should equal(createdAt)
    result.get.updatedAt should equal(updatedAt)
    result.get.versions should equal(2)
  }

  "RoleRepository" should "handle all defined role types" in {
    insertRole(roleType = RoleTypes.Admin)
    insertRole(roleType = RoleTypes.Developer)
    insertRole(roleType = RoleTypes.Viewer)

    RoleTypes.Admin should be(repository.getByType(db, RoleTypes.Admin).get.roleType)
    RoleTypes.Developer should be(repository.getByType(db, RoleTypes.Developer).get.roleType)
    RoleTypes.Viewer should be(repository.getByType(db, RoleTypes.Viewer).get.roleType)
  }

  it should "preserve version numbers" in {
    insertRole(roleType = RoleTypes.Admin, versions = 5)

    val result = repository.getByType(db, RoleTypes.Admin)

    result should be(defined)
    result.get.versions should equal(5)
  }

  it should "preserve timestamps accurately" in {
    val now = System.currentTimeMillis()
    val oneHourAgo = now - (60 * 60 * 1000)

    insertRole(
      roleType = RoleTypes.Admin,
      createdAt = oneHourAgo,
      updatedAt = now,
    )

    val result = repository.getByType(db, RoleTypes.Admin)

    result should be(defined)
    result.get.createdAt should equal(oneHourAgo)
    result.get.updatedAt should equal(now)
  }
}
