package jp.ijufumi.openreports.repositories.system.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.repositories.system.AwsS3Repository
import jp.ijufumi.openreports.repositories.db.StorageRepository
import jp.ijufumi.openreports.exceptions.NotFoundException
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region

import java.io.InputStream

class AwsS3RepositoryImpl @Inject() (storageRepository: StorageRepository) extends AwsS3Repository {
  override def get(workspaceId: String, key: String): InputStream = ???

  override def create(workspaceId: String, key: String, file: InputStream): Unit = ???

  override def delete(workspaceId: String, key: String): Unit = ???

  override def url(workspaceId: String, key: String): String = ???

  private def client(workspaceId: String): S3Client = {
    val storageList = storageRepository.gets(workspaceId)
    if (storageList.isEmpty) {
      throw new NotFoundException(s"storage doesn't exist. workspaceId: ${workspaceId}")
    }
    val storage = storageList.head
    val credentials = AwsBasicCredentials.create(storage.awsAccessKey, storage.awsSecretKeyId)
    val region = Region.of(storage.awsRegion)
    S3Client
      .builder()
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .region(region)
      .build()
  }
}
