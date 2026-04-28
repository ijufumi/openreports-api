package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{DataSource => DataSourceModel}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DataSourceConverterSpec extends AnyFlatSpec with Matchers {
  "toEntity" should "encrypt the password" in {
    val plain = "super-secret-password"
    val model = DataSourceModel(
      id = "id",
      name = "ds",
      url = "jdbc:postgresql://localhost/db",
      username = "user",
      password = plain,
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "ws",
      createdAt = 0,
      updatedAt = 0,
    )

    val entity = DataSourceConverter.toEntity(model)

    entity.password should not equal plain
    entity.password should startWith("v1:")
  }

  "toDomain" should "decrypt the password roundtrip" in {
    val plain = "round-trip-password"
    val model = DataSourceModel(
      id = "id",
      name = "ds",
      url = "jdbc:postgresql://localhost/db",
      username = "user",
      password = plain,
      driverTypeId = "driver-id",
      maxPoolSize = 10,
      workspaceId = "ws",
      createdAt = 0,
      updatedAt = 0,
    )

    val entity = DataSourceConverter.toEntity(model)
    val restored = DataSourceConverter.toDomain(entity)

    restored.password should equal(plain)
  }
}
