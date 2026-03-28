package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.StorageS3
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

class StorageS3RepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new StorageS3RepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("storage_test")
    H2DatabaseHelper.createSchema(db, storageQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, storageQuery)
  }

  "StorageS3RepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new StorageS3RepositoryImpl()
  }

  "getById" should "return storage when exists" in {
    val workspaceId = IDs.ulid()
    val storageId = IDs.ulid()

    val storage = StorageS3(
      id = storageId,
      workspaceId = workspaceId,
      s3BucketName = "test-bucket",
      awsAccessKeyId = "test-access-key",
      awsSecretAccessKey = "test-secret-key",
      awsRegion = "us-east-1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    repository.register(db, storage)

    val result = repository.getById(db, workspaceId, storageId)

    result should be(defined)
    result.get.id should equal(storageId)
    result.get.s3BucketName should equal("test-bucket")
    result.get.awsRegion should equal("us-east-1")
  }

  it should "return None when storage doesn't exist" in {
    val result = repository.getById(db, "workspace-id", "non-existent-id")

    result should be(None)
  }

  "gets" should "return all storages for workspace" in {
    val workspaceId = IDs.ulid()

    val storage1 = StorageS3(
      id = IDs.ulid(),
      workspaceId = workspaceId,
      s3BucketName = "bucket-1",
      awsAccessKeyId = "access-key-1",
      awsSecretAccessKey = "secret-key-1",
      awsRegion = "us-east-1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    val storage2 = StorageS3(
      id = IDs.ulid(),
      workspaceId = workspaceId,
      s3BucketName = "bucket-2",
      awsAccessKeyId = "access-key-2",
      awsSecretAccessKey = "secret-key-2",
      awsRegion = "us-west-2",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    repository.register(db, storage1)
    repository.register(db, storage2)

    val result = repository.gets(db, workspaceId)

    result should not be empty
    result.length should be(2)
  }

  it should "return empty sequence when workspace has no storages" in {
    val result = repository.gets(db, "non-existent-workspace-id")

    result should be(empty)
  }

  "register" should "create new storage and return it" in {
    val storage = StorageS3(
      id = IDs.ulid(),
      workspaceId = IDs.ulid(),
      s3BucketName = "new-bucket",
      awsAccessKeyId = "new-access-key",
      awsSecretAccessKey = "new-secret-key",
      awsRegion = "ap-northeast-1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    val result = repository.register(db, storage)

    result should be(defined)
    result.get.id should equal(storage.id)
    result.get.s3BucketName should equal(storage.s3BucketName)
  }

  "update" should "update existing storage" in {
    val storage = StorageS3(
      id = IDs.ulid(),
      workspaceId = IDs.ulid(),
      s3BucketName = "old-bucket",
      awsAccessKeyId = "old-access-key",
      awsSecretAccessKey = "old-secret-key",
      awsRegion = "us-east-1",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    repository.register(db, storage)

    val updatedStorage = storage.copy(
      s3BucketName = "updated-bucket",
      awsAccessKeyId = "updated-access-key",
      awsRegion = "eu-west-1",
    )

    repository.update(db, updatedStorage)

    val result = repository.getById(db, storage.workspaceId, storage.id)
    result should be(defined)
    result.get.s3BucketName should equal("updated-bucket")
    result.get.awsAccessKeyId should equal("updated-access-key")
    result.get.awsRegion should equal("eu-west-1")
  }
}
