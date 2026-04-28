package jp.ijufumi.openreports.infrastructure.storage.s3.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.domain.repository.StorageS3Repository
import jp.ijufumi.openreports.infrastructure.storage.s3.AwsS3Repository
import jp.ijufumi.openreports.domain.models.entity.{StorageS3 => StorageS3Model}
import slick.jdbc.JdbcBackend.Database
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model._
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model._

import java.nio.file.{Files, Path}
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import scala.util.Using

trait S3ClientFactory {
  def createClient(storage: StorageS3Model): S3Client
  def createPresignerClient(storage: StorageS3Model): S3Presigner
}

class DefaultS3ClientFactory extends S3ClientFactory {
  override def createClient(storage: StorageS3Model): S3Client = {
    val credentials = AwsBasicCredentials.create(storage.awsAccessKeyId, storage.awsSecretAccessKey)
    val region = Region.of(storage.awsRegion)
    S3Client
      .builder()
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .region(region)
      .build()
  }

  override def createPresignerClient(storage: StorageS3Model): S3Presigner = {
    val credentials = AwsBasicCredentials.create(storage.awsAccessKeyId, storage.awsSecretAccessKey)
    val region = Region.of(storage.awsRegion)
    S3Presigner
      .builder()
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .region(region)
      .build()
  }
}

class AwsS3RepositoryImpl @Inject() (
    db: Database,
    storageRepository: StorageS3Repository,
    s3ClientFactory: S3ClientFactory = new DefaultS3ClientFactory(),
) extends AwsS3Repository {
  // 認証情報のローテーション時に古いクライアントを破棄するため、認証情報も含めたキーでキャッシュする
  private val clientCache = new ConcurrentHashMap[String, S3Client]()
  private val presignerCache = new ConcurrentHashMap[String, S3Presigner]()

  override def get(workspaceId: String, key: String): Path = {
    val storage = this.getStorage(workspaceId)
    val request = GetObjectRequest.builder().bucket(storage.s3BucketName).key(key).build()
    val file = Files.createTempFile("", ".tmp")
    Using.resource(client(storage).getObject(request)) { response =>
      Files.copy(response, file, java.nio.file.StandardCopyOption.REPLACE_EXISTING)
    }
    file
  }

  override def create(workspaceId: String, key: String, file: Path): Unit = {
    val storage = this.getStorage(workspaceId)
    val request = PutObjectRequest.builder().bucket(storage.s3BucketName).key(key).build()
    client(storage).putObject(request, file)
  }

  override def delete(workspaceId: String, key: String): Unit = {
    val storage = this.getStorage(workspaceId)
    val request = DeleteObjectRequest.builder().bucket(storage.s3BucketName).key(key).build()
    client(storage).deleteObject(request)
  }

  override def url(workspaceId: String, key: String): String = {
    val storage = this.getStorage(workspaceId)
    val getObjectRequest = GetObjectRequest.builder().bucket(storage.s3BucketName).key(key).build()
    val request = GetObjectPresignRequest
      .builder()
      .getObjectRequest(getObjectRequest)
      .signatureDuration(Duration.ofSeconds(Config.PRESIGNED_URL_EXPIRATION))
      .build()
    presigner(storage).presignGetObject(request).url().toString
  }

  def shutdown(): Unit = {
    clientCache.values().forEach(c => Using(c)(_ => ()))
    clientCache.clear()
    presignerCache.values().forEach(p => Using(p)(_ => ()))
    presignerCache.clear()
  }

  private def client(storage: StorageS3Model): S3Client =
    clientCache.computeIfAbsent(cacheKey(storage), _ => s3ClientFactory.createClient(storage))

  private def presigner(storage: StorageS3Model): S3Presigner =
    presignerCache.computeIfAbsent(
      cacheKey(storage),
      _ => s3ClientFactory.createPresignerClient(storage),
    )

  private def cacheKey(storage: StorageS3Model): String =
    s"${storage.id}:${storage.awsRegion}:${storage.awsAccessKeyId}"

  private def getStorage(workspaceId: String): StorageS3Model = {
    val storageList = storageRepository.gets(db, workspaceId)
    if (storageList.isEmpty) {
      throw new NotFoundException(s"storage doesn't exist. workspaceId: $workspaceId")
    }
    storageList.head
  }
}
