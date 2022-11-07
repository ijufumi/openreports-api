package jp.ijufumi.openreports.repositories.system.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.entities.Storage
import jp.ijufumi.openreports.repositories.system.AwsS3Repository
import jp.ijufumi.openreports.repositories.db.StorageRepository
import jp.ijufumi.openreports.exceptions.NotFoundException
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model._
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model._

import java.nio.file.{Files, Path}
import java.time.Duration
import scala.util.Using

class AwsS3RepositoryImpl @Inject() (storageRepository: StorageRepository) extends AwsS3Repository {
  override def get(workspaceId: String, key: String): Path = {
    val storage = this.getStorage(workspaceId)
    val request = GetObjectRequest.builder().bucket(storage.s3BucketName).key(key).build()
    val file = Files.createTempFile("/tmp", ".tmp")
    val response = Using(this.createClient(storage)) { client =>
      client.getObject(request)
    }.get
    Files.copy(response, file)
    file
  }

  override def create(workspaceId: String, key: String, file: Path): Unit = {
    val storage = this.getStorage(workspaceId)
    val request = PutObjectRequest.builder().bucket(storage.s3BucketName).key(key).build()
    Using(this.createClient(storage)) { client =>
      client.putObject(request, file)
    }
  }

  override def delete(workspaceId: String, key: String): Unit = {
    val storage = this.getStorage(workspaceId)
    val request = DeleteObjectRequest.builder().bucket(storage.s3BucketName).key(key).build()
    Using(this.createClient(storage)) { client =>
      client.deleteObject(request)
    }
  }

  override def url(workspaceId: String, key: String): String = {
    val storage = this.getStorage(workspaceId)
    val getObjectRequest = GetObjectRequest.builder().bucket(storage.s3BucketName).key(key).build()
    val request = GetObjectPresignRequest
      .builder()
      .getObjectRequest(getObjectRequest)
      .signatureDuration(Duration.ofSeconds(Config.PRESIGNED_URL_EXPIRATION))
      .build()
    Using(this.createPresignerClient(storage)) { client =>
      client.presignGetObject(request)
    }.get.url().toString
  }

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

  private def createPresignerClient(storage: Storage): S3Presigner = {
    val credentials = AwsBasicCredentials.create(storage.awsAccessKeyId, storage.awsSecretAccessKey)
    val region = Region.of(storage.awsRegion)
    S3Presigner
      .builder()
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .region(region)
      .build()
  }
}
