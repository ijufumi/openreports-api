package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.RoleFunction
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.infrastructure.persistence.entity.{RoleFunction => RoleFunctionEntity}
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

class RoleFunctionRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new RoleFunctionRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("role_function_test")
    H2DatabaseHelper.createSchema(db, roleFunctionQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, roleFunctionQuery)
  }

  // Helper method to insert test role-function mapping directly into DB
  def insertRoleFunction(
      id: String = IDs.ulid(),
      roleId: String = IDs.ulid(),
      functionId: String = IDs.ulid(),
  ): RoleFunctionEntity = {
    val entity = RoleFunctionEntity(
      id,
      roleId,
      functionId,
      System.currentTimeMillis(),
      System.currentTimeMillis(),
      1,
    )
    Await.result(db.run(roleFunctionQuery += entity), 10.seconds)
    entity
  }

  "RoleFunctionRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new RoleFunctionRepositoryImpl()
  }

  "getAll" should "return all role-function mappings" in {
    val roleId1 = IDs.ulid()
    val roleId2 = IDs.ulid()
    val functionId1 = IDs.ulid()
    val functionId2 = IDs.ulid()

    insertRoleFunction(roleId = roleId1, functionId = functionId1)
    insertRoleFunction(roleId = roleId1, functionId = functionId2)
    insertRoleFunction(roleId = roleId2, functionId = functionId1)

    val result = repository.getAll(db)

    result should have size 3
  }

  it should "return empty sequence when no mappings exist" in {
    val result = repository.getAll(db)

    result should be(empty)
  }

  it should "return mappings with correct role and function IDs" in {
    val roleId = IDs.ulid()
    val functionId = IDs.ulid()

    insertRoleFunction(roleId = roleId, functionId = functionId)

    val result = repository.getAll(db)

    result should have size 1
    result.head.roleId should equal(roleId)
    result.head.functionId should equal(functionId)
  }

  "getByRoleId" should "return all functions for a specific role" in {
    val targetRoleId = IDs.ulid()
    val otherRoleId = IDs.ulid()
    val functionId1 = IDs.ulid()
    val functionId2 = IDs.ulid()
    val functionId3 = IDs.ulid()

    insertRoleFunction(roleId = targetRoleId, functionId = functionId1)
    insertRoleFunction(roleId = targetRoleId, functionId = functionId2)
    insertRoleFunction(roleId = targetRoleId, functionId = functionId3)
    insertRoleFunction(roleId = otherRoleId, functionId = functionId1)

    val result = repository.getByRoleId(db, targetRoleId)

    result should have size 3
    result.map(_.functionId) should contain allOf (functionId1, functionId2, functionId3)
    result.forall(_.roleId == targetRoleId) should be(true)
  }

  it should "return empty sequence when role has no functions" in {
    val roleId = IDs.ulid()
    val otherRoleId = IDs.ulid()
    val functionId = IDs.ulid()

    insertRoleFunction(roleId = otherRoleId, functionId = functionId)

    val result = repository.getByRoleId(db, roleId)

    result should be(empty)
  }

  it should "return empty sequence when role doesn't exist" in {
    val result = repository.getByRoleId(db, "non-existent-role-id")

    result should be(empty)
  }

  it should "handle single function mapping" in {
    val roleId = IDs.ulid()
    val functionId = IDs.ulid()

    insertRoleFunction(roleId = roleId, functionId = functionId)

    val result = repository.getByRoleId(db, roleId)

    result should have size 1
    result.head.roleId should equal(roleId)
    result.head.functionId should equal(functionId)
  }

  it should "handle multiple function mappings for same role" in {
    val roleId = IDs.ulid()
    val functionIds = (1 to 10).map(_ => IDs.ulid())

    functionIds.foreach(functionId => insertRoleFunction(roleId = roleId, functionId = functionId))

    val result = repository.getByRoleId(db, roleId)

    result should have size 10
    result.map(_.functionId) should equal(functionIds)
  }

  "getByFunctionId" should "return all roles for a specific function" in {
    val roleId1 = IDs.ulid()
    val roleId2 = IDs.ulid()
    val roleId3 = IDs.ulid()
    val targetFunctionId = IDs.ulid()
    val otherFunctionId = IDs.ulid()

    insertRoleFunction(roleId = roleId1, functionId = targetFunctionId)
    insertRoleFunction(roleId = roleId2, functionId = targetFunctionId)
    insertRoleFunction(roleId = roleId3, functionId = targetFunctionId)
    insertRoleFunction(roleId = roleId1, functionId = otherFunctionId)

    val result = repository.getByFunctionId(db, targetFunctionId)

    result should have size 3
    result.map(_.roleId) should contain allOf (roleId1, roleId2, roleId3)
    result.forall(_.functionId == targetFunctionId) should be(true)
  }

  it should "return empty sequence when function has no roles" in {
    val functionId = IDs.ulid()
    val otherFunctionId = IDs.ulid()
    val roleId = IDs.ulid()

    insertRoleFunction(roleId = roleId, functionId = otherFunctionId)

    val result = repository.getByFunctionId(db, functionId)

    result should be(empty)
  }

  it should "return empty sequence when function doesn't exist" in {
    val result = repository.getByFunctionId(db, "non-existent-function-id")

    result should be(empty)
  }

  it should "handle single role mapping" in {
    val roleId = IDs.ulid()
    val functionId = IDs.ulid()

    insertRoleFunction(roleId = roleId, functionId = functionId)

    val result = repository.getByFunctionId(db, functionId)

    result should have size 1
    result.head.roleId should equal(roleId)
    result.head.functionId should equal(functionId)
  }

  it should "handle multiple role mappings for same function" in {
    val functionId = IDs.ulid()
    val roleIds = (1 to 10).map(_ => IDs.ulid())

    roleIds.foreach(roleId => insertRoleFunction(roleId = roleId, functionId = functionId))

    val result = repository.getByFunctionId(db, functionId)

    result should have size 10
    result.map(_.roleId) should equal(roleIds)
  }

  "RoleFunction conversion" should "correctly convert from entity to domain model" in {
    val id = IDs.ulid()
    val roleId = IDs.ulid()
    val functionId = IDs.ulid()

    insertRoleFunction(id = id, roleId = roleId, functionId = functionId)

    val result = repository.getAll(db)

    result should have size 1
    result.head.id should equal(id)
    result.head.roleId should equal(roleId)
    result.head.functionId should equal(functionId)
  }

  "RoleFunctionRepository" should "support many-to-many relationship" in {
    val role1 = IDs.ulid()
    val role2 = IDs.ulid()
    val function1 = IDs.ulid()
    val function2 = IDs.ulid()

    // Role1 has Function1 and Function2
    insertRoleFunction(roleId = role1, functionId = function1)
    insertRoleFunction(roleId = role1, functionId = function2)

    // Role2 has Function1 and Function2
    insertRoleFunction(roleId = role2, functionId = function1)
    insertRoleFunction(roleId = role2, functionId = function2)

    val role1Functions = repository.getByRoleId(db, role1)
    val role2Functions = repository.getByRoleId(db, role2)
    val function1Roles = repository.getByFunctionId(db, function1)
    val function2Roles = repository.getByFunctionId(db, function2)

    role1Functions should have size 2
    role2Functions should have size 2
    function1Roles should have size 2
    function2Roles should have size 2
  }

  it should "handle filtering correctly with mixed data" in {
    val adminRoleId = IDs.ulid()
    val userRoleId = IDs.ulid()
    val readFunction = IDs.ulid()
    val writeFunction = IDs.ulid()
    val deleteFunction = IDs.ulid()

    // Admin has all functions
    insertRoleFunction(roleId = adminRoleId, functionId = readFunction)
    insertRoleFunction(roleId = adminRoleId, functionId = writeFunction)
    insertRoleFunction(roleId = adminRoleId, functionId = deleteFunction)

    // User only has read function
    insertRoleFunction(roleId = userRoleId, functionId = readFunction)

    val adminFunctions = repository.getByRoleId(db, adminRoleId)
    val userFunctions = repository.getByRoleId(db, userRoleId)
    val readRoles = repository.getByFunctionId(db, readFunction)
    val writeRoles = repository.getByFunctionId(db, writeFunction)
    val deleteRoles = repository.getByFunctionId(db, deleteFunction)

    adminFunctions should have size 3
    userFunctions should have size 1
    readRoles should have size 2
    writeRoles should have size 1
    deleteRoles should have size 1
  }
}
