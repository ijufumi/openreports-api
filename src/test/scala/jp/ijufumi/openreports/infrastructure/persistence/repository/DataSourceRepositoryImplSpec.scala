package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.{DataSource, DriverType}
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.infrastructure.persistence.entity.{DriverType => DriverTypeEntity}
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database

class DataSourceRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new DataSourceRepositoryImpl()
  val driverTypeRepository = new DriverTypeRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("datasource_test")
    H2DatabaseHelper.createSchema(db, dataSourceQuery, driverTypeQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, dataSourceQuery, driverTypeQuery)
  }

  // Helper method to create a test driver type
  def createTestDriverType(id: String = IDs.ulid(), name: String = "PostgreSQL"): DriverType = {
    import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
    import scala.concurrent.Await
    import scala.concurrent.duration._
    import slick.jdbc.PostgresProfile.api._

    val jdbcDriverClass =
      if (name.contains("MySQL")) JdbcDriverClasses.MySQL else JdbcDriverClasses.Postgres
    val entity = DriverTypeEntity(id, name, jdbcDriverClass)
    Await.result(db.run(driverTypeQuery += entity), 10.seconds)

    DriverType(id, name, jdbcDriverClass)
  }

  // Helper method to create a test data source
  def createTestDataSource(
      workspaceId: String,
      driverTypeId: String,
      name: String = "Test DataSource",
  ): DataSource = {
    DataSource(
      id = IDs.ulid(),
      name = name,
      url = "jdbc:postgresql://localhost:5432/testdb",
      username = "testuser",
      password = "testpass",
      driverTypeId = driverTypeId,
      maxPoolSize = 10,
      workspaceId = workspaceId,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )
  }

  "DataSourceRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new DataSourceRepositoryImpl()
  }

  "register" should "create new data source and return it" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()
    val dataSource = createTestDataSource(workspaceId, driverType.id)

    val result = repository.register(db, dataSource)

    result should be(defined)
    result.get.id should equal(dataSource.id)
    result.get.name should equal(dataSource.name)
    result.get.url should equal(dataSource.url)
  }

  it should "persist data source to database" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()
    val dataSource = createTestDataSource(workspaceId, driverType.id)

    repository.register(db, dataSource)

    val retrieved = repository.getById(db, workspaceId, dataSource.id)
    retrieved should be(defined)
    retrieved.get.name should equal(dataSource.name)
  }

  "getById" should "return data source when exists" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()
    val dataSource = createTestDataSource(workspaceId, driverType.id)
    repository.register(db, dataSource)

    val result = repository.getById(db, workspaceId, dataSource.id)

    result should be(defined)
    result.get.id should equal(dataSource.id)
    result.get.workspaceId should equal(workspaceId)
  }

  it should "filter by workspace ID" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()
    val driverType = createTestDriverType()

    val dataSource1 = createTestDataSource(workspaceId1, driverType.id)
    val dataSource2 = createTestDataSource(workspaceId2, driverType.id)

    repository.register(db, dataSource1)
    repository.register(db, dataSource2)

    val result = repository.getById(db, workspaceId1, dataSource1.id)

    result should be(defined)
    result.get.workspaceId should equal(workspaceId1)
  }

  "getAll" should "return all data sources for workspace" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()

    val dataSource1 = createTestDataSource(workspaceId, driverType.id, "DataSource 1")
    val dataSource2 = createTestDataSource(workspaceId, driverType.id, "DataSource 2")

    repository.register(db, dataSource1)
    repository.register(db, dataSource2)

    val result = repository.getAll(db, workspaceId)

    result should have size 2
    result.map(_.name) should contain allOf ("DataSource 1", "DataSource 2")
  }

  it should "return empty sequence when no data sources exist" in {
    val workspaceId = IDs.ulid()

    val result = repository.getAll(db, workspaceId)

    result should be(empty)
  }

  it should "filter by workspace ID" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()
    val driverType = createTestDriverType()

    val dataSource1 = createTestDataSource(workspaceId1, driverType.id)
    val dataSource2 = createTestDataSource(workspaceId2, driverType.id)

    repository.register(db, dataSource1)
    repository.register(db, dataSource2)

    val result = repository.getAll(db, workspaceId1)

    result should have size 1
    result.head.workspaceId should equal(workspaceId1)
  }

  "getByIdWithDriverType" should "return data source with driver type" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()
    val dataSource = createTestDataSource(workspaceId, driverType.id)
    repository.register(db, dataSource)

    val result = repository.getByIdWithDriverType(db, workspaceId, dataSource.id)

    result should be(defined)
    result.get.id should equal(dataSource.id)
    result.get.driverType should be(defined)
    result.get.driverType.get.id should equal(driverType.id)
    result.get.driverType.get.name should equal(driverType.name)
  }

  it should "return None when data source doesn't exist" in {
    val workspaceId = IDs.ulid()

    val result = repository.getByIdWithDriverType(db, workspaceId, "non-existent-id")

    result should be(None)
  }

  "getAllWithDriverType" should "return all data sources with driver types" in {
    val workspaceId = IDs.ulid()
    val driverType1 = createTestDriverType(name = "PostgreSQL")
    val driverType2 = createTestDriverType(name = "MySQL")

    val dataSource1 = createTestDataSource(workspaceId, driverType1.id, "PostgreSQL DataSource")
    val dataSource2 = createTestDataSource(workspaceId, driverType2.id, "MySQL DataSource")

    repository.register(db, dataSource1)
    repository.register(db, dataSource2)

    val result = repository.getAllWithDriverType(db, workspaceId)

    result should have size 2
    result.foreach { ds =>
      ds.driverType should be(defined)
    }
  }

  it should "return empty sequence when no data sources exist" in {
    val workspaceId = IDs.ulid()

    val result = repository.getAllWithDriverType(db, workspaceId)

    result should be(empty)
  }

  "update" should "update existing data source" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()
    val dataSource = createTestDataSource(workspaceId, driverType.id)
    repository.register(db, dataSource)

    val updatedDataSource = dataSource.copy(
      name = "Updated Name",
      url = "jdbc:postgresql://newhost:5432/newdb",
    )

    repository.update(db, updatedDataSource)

    val result = repository.getById(db, workspaceId, dataSource.id)
    result should be(defined)
    result.get.name should equal("Updated Name")
    result.get.url should equal("jdbc:postgresql://newhost:5432/newdb")
  }

  "delete" should "remove data source from database" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()
    val dataSource = createTestDataSource(workspaceId, driverType.id)
    repository.register(db, dataSource)

    repository.delete(db, workspaceId, dataSource.id)

    // Verify it's deleted by checking getAll doesn't return it
    val allDataSources = repository.getAll(db, workspaceId)
    allDataSources should be(empty)
  }

  it should "only delete data sources for specified workspace" in {
    val workspaceId1 = IDs.ulid()
    val workspaceId2 = IDs.ulid()
    val driverType = createTestDriverType()

    val dataSource1 = createTestDataSource(workspaceId1, driverType.id)
    val dataSource2 = createTestDataSource(workspaceId2, driverType.id)

    repository.register(db, dataSource1)
    repository.register(db, dataSource2)

    repository.delete(db, workspaceId1, dataSource1.id)

    val workspace1DataSources = repository.getAll(db, workspaceId1)
    val workspace2DataSources = repository.getAll(db, workspaceId2)

    workspace1DataSources should be(empty)
    workspace2DataSources should have size 1
  }

  "DataSourceRepository" should "handle special characters in fields" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()
    val dataSource = createTestDataSource(workspaceId, driverType.id).copy(
      name = "Test's \"DataSource\" with special chars!",
      username = "user@domain.com",
      password = "p@$$w0rd!#$%",
    )

    val result = repository.register(db, dataSource)

    result should be(defined)
    result.get.name should equal("Test's \"DataSource\" with special chars!")
    result.get.username should equal("user@domain.com")
    result.get.password should equal("p@$$w0rd!#$%")
  }

  it should "handle very long field values" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()
    val longName = "A" * 200
    val dataSource = createTestDataSource(workspaceId, driverType.id).copy(name = longName)

    val result = repository.register(db, dataSource)

    result should be(defined)
    result.get.name should have length 200
  }

  it should "handle empty string in optional fields" in {
    val workspaceId = IDs.ulid()
    val driverType = createTestDriverType()
    val dataSource = createTestDataSource(workspaceId, driverType.id).copy(
      username = "",
      password = "",
    )

    val result = repository.register(db, dataSource)

    result should be(defined)
    result.get.username should equal("")
    result.get.password should equal("")
  }
}
