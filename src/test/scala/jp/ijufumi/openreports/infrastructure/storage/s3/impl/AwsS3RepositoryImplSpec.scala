package jp.ijufumi.openreports.infrastructure.storage.s3.impl

import jp.ijufumi.openreports.domain.repository.StorageS3Repository
import jp.ijufumi.openreports.domain.models.entity.{StorageS3 => StorageS3Model}
import jp.ijufumi.openreports.exceptions.NotFoundException
import org.mockito.ArgumentMatchers.{any, eq => meq}
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class AwsS3RepositoryImplSpec extends AnyFlatSpec with Matchers with MockitoSugar {

  "AwsS3RepositoryImpl" should "be instantiable" in {
    val db = mock[Database]
    val storageRepository = mock[StorageS3Repository]

    noException should be thrownBy new AwsS3RepositoryImpl(db, storageRepository)
  }

  "getStorage" should "throw NotFoundException when storage doesn't exist" in {
    val db = mock[Database]
    val storageRepository = mock[StorageS3Repository]
    val repository = new AwsS3RepositoryImpl(db, storageRepository)
    val workspaceId = "workspace-id"

    when(storageRepository.gets(db, workspaceId)).thenReturn(Seq.empty)

    assertThrows[NotFoundException] {
      // This will call the private getStorage method through public methods
      repository.get(workspaceId, "test-key")
    }
  }

  // Note: The following tests require AWS SDK mocking or LocalStack for integration testing
  // They are commented out as they depend on external AWS services
  // For proper testing, consider:
  // 1. Using LocalStack for S3 integration tests
  // 2. Mocking AWS SDK clients (requires refactoring to inject S3Client)
  // 3. Using testcontainers with LocalStack

  /*
  "get" should "retrieve file from S3 bucket" in {
    val db = mock[Database]
    val storageRepository = mock[StorageS3Repository]
    val repository = new AwsS3RepositoryImpl(db, storageRepository)

    val workspaceId = "workspace-id"
    val key = "test-file.txt"
    val storage = StorageS3Model(
      id = "storage-id",
      workspaceId = workspaceId,
      s3BucketName = "test-bucket",
      awsAccessKeyId = "test-access-key",
      awsSecretAccessKey = "test-secret-key",
      awsRegion = "us-east-1",
      createdAt = 0,
      updatedAt = 0
    )

    when(storageRepository.gets(db, workspaceId)).thenReturn(Seq(storage))

    val path = repository.get(workspaceId, key)
    path should not be null
    Files.exists(path) should be(true)
  }

  "create" should "upload file to S3 bucket" in {
    val db = mock[Database]
    val storageRepository = mock[StorageS3Repository]
    val repository = new AwsS3RepositoryImpl(db, storageRepository)

    val workspaceId = "workspace-id"
    val key = "upload-file.txt"
    val tempFile = Files.createTempFile("test", ".txt")
    Files.write(tempFile, "test content".getBytes)

    val storage = StorageS3Model(
      id = "storage-id",
      workspaceId = workspaceId,
      s3BucketName = "test-bucket",
      awsAccessKeyId = "test-access-key",
      awsSecretAccessKey = "test-secret-key",
      awsRegion = "us-east-1",
      createdAt = 0,
      updatedAt = 0
    )

    when(storageRepository.gets(db, workspaceId)).thenReturn(Seq(storage))

    noException should be thrownBy repository.create(workspaceId, key, tempFile)

    Files.deleteIfExists(tempFile)
  }

  "delete" should "remove file from S3 bucket" in {
    val db = mock[Database]
    val storageRepository = mock[StorageS3Repository]
    val repository = new AwsS3RepositoryImpl(db, storageRepository)

    val workspaceId = "workspace-id"
    val key = "delete-file.txt"
    val storage = StorageS3Model(
      id = "storage-id",
      workspaceId = workspaceId,
      s3BucketName = "test-bucket",
      awsAccessKeyId = "test-access-key",
      awsSecretAccessKey = "test-secret-key",
      awsRegion = "us-east-1",
      createdAt = 0,
      updatedAt = 0
    )

    when(storageRepository.gets(db, workspaceId)).thenReturn(Seq(storage))

    noException should be thrownBy repository.delete(workspaceId, key)
  }

  "url" should "generate presigned URL for S3 object" in {
    val db = mock[Database]
    val storageRepository = mock[StorageS3Repository]
    val repository = new AwsS3RepositoryImpl(db, storageRepository)

    val workspaceId = "workspace-id"
    val key = "presigned-file.txt"
    val storage = StorageS3Model(
      id = "storage-id",
      workspaceId = workspaceId,
      s3BucketName = "test-bucket",
      awsAccessKeyId = "test-access-key",
      awsSecretAccessKey = "test-secret-key",
      awsRegion = "us-east-1",
      createdAt = 0,
      updatedAt = 0
    )

    when(storageRepository.gets(db, workspaceId)).thenReturn(Seq(storage))

    val url = repository.url(workspaceId, key)
    url should not be empty
    url should include("amazonaws.com")
  }
  */
}
