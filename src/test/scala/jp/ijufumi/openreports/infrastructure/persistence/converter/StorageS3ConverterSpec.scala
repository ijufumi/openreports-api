package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{StorageS3 => StorageS3Model}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StorageS3ConverterSpec extends AnyFlatSpec with Matchers {
  "toEntity" should "encrypt the access key id and secret access key" in {
    val accessKey = "AKIAEXAMPLEACCESSKEY"
    val secret = "exampleSecretAccessKey1234"
    val model = StorageS3Model(
      id = "id",
      workspaceId = "ws",
      awsAccessKeyId = accessKey,
      awsSecretAccessKey = secret,
      awsRegion = "us-east-1",
      s3BucketName = "bucket",
      createdAt = 0,
      updatedAt = 0,
    )

    val entity = StorageS3Converter.toEntity(model)

    entity.awsAccessKeyId should not equal accessKey
    entity.awsAccessKeyId should startWith("v1:")
    entity.awsSecretAccessKey should not equal secret
    entity.awsSecretAccessKey should startWith("v1:")
  }

  "toDomain" should "decrypt credentials roundtrip" in {
    val accessKey = "AKIAEXAMPLEACCESSKEY"
    val secret = "exampleSecretAccessKey1234"
    val model = StorageS3Model(
      id = "id",
      workspaceId = "ws",
      awsAccessKeyId = accessKey,
      awsSecretAccessKey = secret,
      awsRegion = "us-east-1",
      s3BucketName = "bucket",
      createdAt = 0,
      updatedAt = 0,
    )

    val entity = StorageS3Converter.toEntity(model)
    val restored = StorageS3Converter.toDomain(entity)

    restored.awsAccessKeyId should equal(accessKey)
    restored.awsSecretAccessKey should equal(secret)
  }
}
