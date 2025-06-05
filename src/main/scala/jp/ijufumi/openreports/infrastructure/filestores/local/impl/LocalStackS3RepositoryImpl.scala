package jp.ijufumi.openreports.infrastructure.filestores.local.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.StorageS3Repository
import jp.ijufumi.openreports.infrastructure.filestores.local.LocalStackS3Repository
import slick.jdbc.JdbcBackend.Database
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model._
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model._

import java.nio.file.{Files, Path}
import java.time.Duration
import scala.util.Using

class LocalStackS3RepositoryImpl @Inject()(db: Database, storageRepository: StorageS3Repository)
    extends LocalStackS3Repository {
  override def get(workspaceId: String, key: String): Path = {
    val request = GetObjectRequest.builder().bucket(Config.LOCALSTACK_S3_BUCKET).key(key).build()
    val file = Files.createTempFile("/tmp", ".tmp")
    val response = Using(this.createClient()) { client =>
      client.getObject(request)
    }.get
    Files.copy(response, file)
    file
  }

  override def create(workspaceId: String, key: String, file: Path): Unit = {
    val request = PutObjectRequest.builder().bucket(Config.LOCALSTACK_S3_BUCKET).key(key).build()
    Using(this.createClient()) { client =>
      client.putObject(request, file)
    }
  }

  override def delete(workspaceId: String, key: String): Unit = {
    val request = DeleteObjectRequest.builder().bucket(Config.LOCALSTACK_S3_BUCKET).key(key).build()
    Using(this.createClient()) { client =>
      client.deleteObject(request)
    }
  }

  override def url(workspaceId: String, key: String): String = {
    val getObjectRequest =
      GetObjectRequest.builder().bucket(Config.LOCALSTACK_S3_BUCKET).key(key).build()
    val request = GetObjectPresignRequest
      .builder()
      .getObjectRequest(getObjectRequest)
      .signatureDuration(Duration.ofSeconds(Config.PRESIGNED_URL_EXPIRATION))
      .build()
    Using(this.createPresignerClient()) { client =>
      client.presignGetObject(request)
    }.get.url().toString
  }

  private def createClient(): S3Client = {
    val credentials =
      AwsBasicCredentials.create(Config.LOCALSTACK_S3_ACCESS_KEY, Config.LOCALSTACK_S3_SECRET_KEY)
    val region = Region.of(Config.LOCALSTACK_S3_REGION)
    S3Client
      .builder()
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .region(region)
      .build()
  }

  private def createPresignerClient(): S3Presigner = {
    val credentials =
      AwsBasicCredentials.create(Config.LOCALSTACK_S3_ACCESS_KEY, Config.LOCALSTACK_S3_SECRET_KEY)
    val region = Region.of(Config.LOCALSTACK_S3_REGION)
    S3Presigner
      .builder()
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .region(region)
      .build()
  }
}
