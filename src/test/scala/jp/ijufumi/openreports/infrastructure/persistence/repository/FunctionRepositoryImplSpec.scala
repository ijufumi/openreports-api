package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.Function
import jp.ijufumi.openreports.domain.models.value.enums.ActionTypes
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Function => FunctionEntity}
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

class FunctionRepositoryImplSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  var db: Database = _
  val repository = new FunctionRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("function_test")
    H2DatabaseHelper.createSchema(db, functionQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, functionQuery)
  }

  // Helper method to insert test function directly into DB
  def insertFunction(
      id: String = IDs.ulid(),
      resource: String = "reports",
      action: ActionTypes.ActionType = ActionTypes.Reference
  ): FunctionEntity = {
    val entity = FunctionEntity(
      id,
      resource,
      action,
      System.currentTimeMillis(),
      System.currentTimeMillis(),
      1
    )
    Await.result(db.run(functionQuery += entity), 10.seconds)
    entity
  }

  "FunctionRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new FunctionRepositoryImpl()
  }

  "getAll" should "return all functions" in {
    insertFunction(resource = "reports", action = ActionTypes.Reference)
    insertFunction(resource = "reports", action = ActionTypes.Create)
    insertFunction(resource = "templates", action = ActionTypes.Reference)

    val result = repository.getAll(db)

    result should have size 3
    result.map(_.resource) should contain allOf ("reports", "templates")
  }

  it should "return empty sequence when no functions exist" in {
    val result = repository.getAll(db)

    result should be(empty)
  }

  it should "return functions with correct action types" in {
    insertFunction(resource = "reports", action = ActionTypes.Create)
    insertFunction(resource = "reports", action = ActionTypes.Update)
    insertFunction(resource = "reports", action = ActionTypes.Delete)
    insertFunction(resource = "reports", action = ActionTypes.Reference)

    val result = repository.getAll(db)

    result should have size 4
    result.map(_.action) should contain allOf (
      ActionTypes.Create,
      ActionTypes.Update,
      ActionTypes.Delete,
      ActionTypes.Reference
    )
  }

  it should "return functions with correct resource names" in {
    insertFunction(resource = "reports", action = ActionTypes.Reference)
    insertFunction(resource = "templates", action = ActionTypes.Create)
    insertFunction(resource = "datasources", action = ActionTypes.Reference)

    val result = repository.getAll(db)

    result.map(_.resource) should contain allOf ("reports", "templates", "datasources")
  }

  "getsByIds" should "return functions matching provided IDs" in {
    val id1 = IDs.ulid()
    val id2 = IDs.ulid()
    val id3 = IDs.ulid()

    insertFunction(id = id1, resource = "reports", action = ActionTypes.Reference)
    insertFunction(id = id2, resource = "templates", action = ActionTypes.Create)
    insertFunction(id = id3, resource = "datasources", action = ActionTypes.Update)

    val result = repository.getsByIds(db, Seq(id1, id3))

    result should have size 2
    result.map(_.id) should contain allOf (id1, id3)
    result.map(_.id) should not contain id2
  }

  it should "return empty sequence when no IDs match" in {
    insertFunction(resource = "reports", action = ActionTypes.Reference)

    val result = repository.getsByIds(db, Seq("non-existent-id-1", "non-existent-id-2"))

    result should be(empty)
  }

  it should "return empty sequence when IDs list is empty" in {
    insertFunction(resource = "reports", action = ActionTypes.Reference)

    val result = repository.getsByIds(db, Seq.empty)

    result should be(empty)
  }

  it should "handle single ID lookup" in {
    val targetId = IDs.ulid()
    insertFunction(id = targetId, resource = "reports", action = ActionTypes.Reference)
    insertFunction(resource = "templates", action = ActionTypes.Create)

    val result = repository.getsByIds(db, Seq(targetId))

    result should have size 1
    result.head.id should equal(targetId)
  }

  it should "handle multiple IDs lookup" in {
    val ids = (1 to 10).map(_ => IDs.ulid())
    ids.foreach(id => insertFunction(id = id, resource = "test", action = ActionTypes.Reference))

    val lookupIds = ids.take(5)
    val result = repository.getsByIds(db, lookupIds)

    result should have size 5
    result.sortBy(_.id)
    result.map(_.id).count(id => lookupIds.contains(id)) should equal(5)
  }

  it should "preserve order when IDs are returned (though not guaranteed by implementation)" in {
    val id1 = IDs.ulid()
    Thread.sleep(10)
    val id2 = IDs.ulid()
    Thread.sleep(10)
    val id3 = IDs.ulid()

    insertFunction(id = id1, resource = "reports", action = ActionTypes.Reference)
    insertFunction(id = id2, resource = "templates", action = ActionTypes.Create)
    insertFunction(id = id3, resource = "datasources", action = ActionTypes.Update)

    val result = repository.getsByIds(db, Seq(id1, id2, id3))

    result should have size 3
    result.map(_.id) should contain allOf (id1, id2, id3)
  }

  "Function conversion" should "correctly convert from entity to domain model" in {
    val id = IDs.ulid()

    insertFunction(
      id = id,
      resource = "reports",
      action = ActionTypes.Reference
    )

    val result = repository.getAll(db)

    result should have size 1
    result.head.id should equal(id)
    result.head.resource should equal("reports")
    result.head.action should equal(ActionTypes.Reference)
  }

  "FunctionRepository" should "support all action types" in {
    insertFunction(resource = "test", action = ActionTypes.Create)
    insertFunction(resource = "test", action = ActionTypes.Update)
    insertFunction(resource = "test", action = ActionTypes.Delete)
    insertFunction(resource = "test", action = ActionTypes.Reference)

    val result = repository.getAll(db)

    result should have size 4
    result.map(_.action) should contain allOf (
      ActionTypes.Create,
      ActionTypes.Update,
      ActionTypes.Delete,
      ActionTypes.Reference
    )
  }

  it should "handle special characters in resource names" in {
    insertFunction(resource = "special-resource_name.123", action = ActionTypes.Reference)

    val result = repository.getAll(db)

    result should have size 1
    result.head.resource should equal("special-resource_name.123")
  }
}
