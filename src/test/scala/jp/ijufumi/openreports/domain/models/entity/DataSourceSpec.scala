package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import jp.ijufumi.openreports.presentation.request.UpdateDataSource
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DataSourceSpec extends AnyFlatSpec with Matchers {

  "DataSource" should "be creatable with valid fields" in {
    val dataSource = DataSource(
      id = IDs.ulid(),
      name = "PostgreSQL Database",
      url = "jdbc:postgresql://localhost:5432/testdb",
      username = "testuser",
      password = "testpass",
      driverTypeId = "driver-type-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    dataSource should not be null
    dataSource.name should equal("PostgreSQL Database")
    dataSource.url should include("postgresql")
  }

  it should "maintain immutability" in {
    val ds1 = DataSource(
      id = "ds-id",
      name = "Original Name",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val ds2 = ds1.copy(name = "Updated Name")

    ds1.name should equal("Original Name")
    ds2.name should equal("Updated Name")
    ds1.id should equal(ds2.id)
  }

  it should "handle various JDBC URLs" in {
    val urls = Seq(
      "jdbc:postgresql://localhost:5432/mydb",
      "jdbc:mysql://127.0.0.1:3306/testdb",
      "jdbc:postgresql://db.example.com:5432/production",
      "jdbc:h2:mem:testdb",
      "jdbc:oracle:thin:@localhost:1521:orcl"
    )

    urls.foreach { url =>
      val ds = DataSource(
        id = IDs.ulid(),
        name = "Test DB",
        url = url,
        username = "user",
        password = "pass",
        driverTypeId = "driver-id",
        maxPoolSize = 10,
        workspaceId = "workspace-id",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
      )

      ds.url should equal(url)
    }
  }

  it should "handle special characters in password" in {
    val specialPassword = "p@$$w0rd!#$%^&*()"
    val ds = DataSource(
      id = IDs.ulid(),
      name = "Test DB",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = specialPassword,
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    ds.password should equal(specialPassword)
  }

  it should "handle special characters in username" in {
    val specialUsername = "user@domain.com"
    val ds = DataSource(
      id = IDs.ulid(),
      name = "Test DB",
      url = "jdbc:postgresql://localhost:5432/db",
      username = specialUsername,
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    ds.username should equal(specialUsername)
  }

  it should "handle empty credentials" in {
    val ds = DataSource(
      id = IDs.ulid(),
      name = "Test DB",
      url = "jdbc:h2:mem:testdb",
      username = "",
      password = "",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    ds.username should equal("")
    ds.password should equal("")
  }

  it should "handle various max pool sizes" in {
    val poolSizes = Seq(1, 5, 10, 20, 50, 100)

    poolSizes.foreach { size =>
      val ds = DataSource(
        id = IDs.ulid(),
        name = "Test DB",
        url = "jdbc:postgresql://localhost:5432/db",
        username = "user",
        password = "pass",
        driverTypeId = "driver-id",
        maxPoolSize = size,
        workspaceId = "workspace-id",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
      )

      ds.maxPoolSize should equal(size)
    }
  }

  it should "include driverType when provided" in {
    val driverType = DriverType(
      id = "driver-id",
      name = "PostgreSQL",
      jdbcDriverClass = JdbcDriverClasses.Postgres
    )

    val ds = DataSource(
      id = IDs.ulid(),
      name = "Test DB",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
      driverType = Some(driverType)
    )

    ds.driverType should be(defined)
    ds.driverType.get.id should equal("driver-id")
    ds.driverType.get.name should equal("PostgreSQL")
  }

  it should "handle None for driverType" in {
    val ds = DataSource(
      id = IDs.ulid(),
      name = "Test DB",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
      driverType = None
    )

    ds.driverType should be(None)
  }

  it should "preserve timestamps" in {
    val createdAt = System.currentTimeMillis()
    val updatedAt = createdAt + 1000

    val ds = DataSource(
      id = IDs.ulid(),
      name = "Test DB",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = createdAt,
      updatedAt = updatedAt
    )

    ds.createdAt should equal(createdAt)
    ds.updatedAt should equal(updatedAt)
    ds.updatedAt should be > ds.createdAt
  }

  it should "support copy with modifications" in {
    val original = DataSource(
      id = "ds-id",
      name = "Original DB",
      url = "jdbc:postgresql://localhost:5432/db1",
      username = "user1",
      password = "pass1",
      driverTypeId = "driver-id-1",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val modified = original.copy(
      name = "Modified DB",
      url = "jdbc:postgresql://localhost:5432/db2",
      username = "user2",
      password = "pass2",
      updatedAt = 2000L
    )

    modified.id should equal(original.id)
    modified.name should equal("Modified DB")
    modified.url should equal("jdbc:postgresql://localhost:5432/db2")
    modified.username should equal("user2")
    modified.password should equal("pass2")
    modified.updatedAt should equal(2000L)
    modified.createdAt should equal(original.createdAt)
  }

  "DataSource copyForUpdate" should "update only name field" in {
    val original = DataSource(
      id = "ds-id",
      name = "Original Name",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 2000L
    )

    val updateRequest = UpdateDataSource(name = "Updated Name")
    val updated = original.copyForUpdate(updateRequest)

    updated.id should equal(original.id)
    updated.name should equal("Updated Name")
    updated.url should equal(original.url)
    updated.username should equal(original.username)
    updated.password should equal(original.password)
  }

  "DataSource toResponse" should "include driver type when present" in {
    val driverType = DriverType(
      id = "driver-id",
      name = "PostgreSQL",
      jdbcDriverClass = JdbcDriverClasses.Postgres
    )

    val ds = DataSource(
      id = "ds-id",
      name = "Test DB",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "secret",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
      driverType = Some(driverType)
    )

    val response = ds.toResponse

    response.id should equal("ds-id")
    response.name should equal("Test DB")
    response.driverType should be(defined)
  }

  "DataSource equality" should "compare by value" in {
    val ds1 = DataSource(
      id = "same-id",
      name = "Test",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val ds2 = DataSource(
      id = "same-id",
      name = "Test",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    ds1 should equal(ds2)
  }

  it should "differentiate when values differ" in {
    val ds1 = DataSource(
      id = "id-1",
      name = "Test",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val ds2 = DataSource(
      id = "id-2",
      name = "Test",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = "pass",
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    ds1 should not equal ds2
  }

  "DataSource security" should "store password as-is (assumes encryption at higher layer)" in {
    val plainPassword = "my-secret-password"
    val ds = DataSource(
      id = IDs.ulid(),
      name = "Test DB",
      url = "jdbc:postgresql://localhost:5432/db",
      username = "user",
      password = plainPassword,
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    // Password is stored as-is in the entity
    // Encryption/decryption should happen at service/repository layer
    ds.password should equal(plainPassword)
  }
}
