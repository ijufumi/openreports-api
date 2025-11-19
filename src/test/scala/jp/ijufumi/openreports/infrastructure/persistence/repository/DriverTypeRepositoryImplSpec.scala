package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.DriverType
import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.infrastructure.persistence.entity.{DriverType => DriverTypeEntity}
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

class DriverTypeRepositoryImplSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  var db: Database = _
  val repository = new DriverTypeRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("drivertype_test")
    H2DatabaseHelper.createSchema(db, driverTypeQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, driverTypeQuery)
  }

  // Helper method to insert test driver type directly into DB
  def insertDriverType(
      id: String = IDs.ulid(),
      name: String = "PostgreSQL",
      jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass = JdbcDriverClasses.Postgres
  ): DriverTypeEntity = {
    val entity = DriverTypeEntity(id, name, jdbcDriverClass)
    Await.result(db.run(driverTypeQuery += entity), 10.seconds)
    entity
  }

  "DriverTypeRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new DriverTypeRepositoryImpl()
  }

  "getAll" should "return all driver types" in {
    insertDriverType(name = "PostgreSQL", jdbcDriverClass = JdbcDriverClasses.Postgres)
    insertDriverType(name = "MySQL", jdbcDriverClass = JdbcDriverClasses.MySQL)

    val result = repository.getAll(db)

    result should have size 2
    result.map(_.name) should contain allOf ("PostgreSQL", "MySQL")
  }

  it should "return empty sequence when no driver types exist" in {
    val result = repository.getAll(db)

    result should be(empty)
  }

  it should "return driver types with correct jdbc driver classes" in {
    insertDriverType(name = "PostgreSQL", jdbcDriverClass = JdbcDriverClasses.Postgres)
    insertDriverType(name = "MySQL", jdbcDriverClass = JdbcDriverClasses.MySQL)

    val result = repository.getAll(db)

    val postgres = result.find(_.name == "PostgreSQL")
    val mysql = result.find(_.name == "MySQL")

    postgres should be(defined)
    postgres.get.jdbcDriverClass should equal(JdbcDriverClasses.Postgres)

    mysql should be(defined)
    mysql.get.jdbcDriverClass should equal(JdbcDriverClasses.MySQL)
  }

  it should "return driver types with correct IDs" in {
    val id1 = IDs.ulid()
    val id2 = IDs.ulid()

    insertDriverType(id = id1, name = "PostgreSQL")
    insertDriverType(id = id2, name = "MySQL")

    val result = repository.getAll(db)

    result.map(_.id) should contain allOf (id1, id2)
  }

  it should "handle multiple calls consistently" in {
    insertDriverType(name = "PostgreSQL")
    insertDriverType(name = "MySQL")

    val result1 = repository.getAll(db)
    val result2 = repository.getAll(db)

    result1 should have size 2
    result2 should have size 2
    result1.map(_.id) should equal(result2.map(_.id))
  }

  it should "handle special characters in driver type name" in {
    insertDriverType(name = "PostgreSQL 15 (Special Edition)")

    val result = repository.getAll(db)

    result should have size 1
    result.head.name should equal("PostgreSQL 15 (Special Edition)")
  }

  it should "handle unicode characters in name" in {
    insertDriverType(name = "データベースドライバー")

    val result = repository.getAll(db)

    result should have size 1
    result.head.name should equal("データベースドライバー")
  }

  "DriverType conversion" should "correctly convert from entity to domain model" in {
    val id = IDs.ulid()
    insertDriverType(id = id, name = "PostgreSQL", jdbcDriverClass = JdbcDriverClasses.Postgres)

    val result = repository.getAll(db)

    result should have size 1
    result.head.id should equal(id)
    result.head.name should equal("PostgreSQL")
    result.head.jdbcDriverClass should equal(JdbcDriverClasses.Postgres)
  }
}
