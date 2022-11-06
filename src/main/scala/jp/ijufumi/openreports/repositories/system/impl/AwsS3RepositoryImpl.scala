package jp.ijufumi.openreports.repositories.system.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.Storage
import jp.ijufumi.openreports.repositories.system.AwsS3Repository
import jp.ijufumi.openreports.repositories.db.StorageRepository
import jp.ijufumi.openreports.exceptions.NotFoundException
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model._

import java.io.InputStream

class AwsS3RepositoryImpl @Inject() (storageRepository: StorageRepository) extends AwsS3Repository {
  override def get(workspaceId: String, key: String): InputStream = {
    val storage = this.getStorage(workspaceId)
    val client = this.createClient(storage)
    val request = GetObjectRequest.builder().bucket(storage.s3BucketName).key(key).build()
    client.getObject(request)
  }

  override def create(workspaceId: String, key: String, file: InputStream): Unit = ???

  override def delete(workspaceId: String, key: String): Unit = ???

  override def url(workspaceId: String, key: String): String = ???

  private def getStorage(workspaceId: String): Storage = {
    val storageList = storageRepository.gets(workspaceId)
    if (storageList.isEmpty) {
      throw new NotFoundException(s"storage doesn't exist. workspaceId: ${workspaceId}")
    }
    storageList.head
  }

  private def createClient(storage: Storage): S3Client = {
    val credentials = AwsBasicCredentials.create(storage.awsAccessKeyId, storage.awsSecretAccessKey)
    val region = Region.of(storage.awsRegion)
    S3Client
      .builder()
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .region(region)
      .build()
  }
}
