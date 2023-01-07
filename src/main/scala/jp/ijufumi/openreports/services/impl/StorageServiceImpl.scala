package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.services.StorageService
import jp.ijufumi.openreports.entities.enums.StorageTypes
import jp.ijufumi.openreports.entities.enums.StorageTypes.StorageType
import jp.ijufumi.openreports.gateways.filestores.local.LocalFileRepository
import jp.ijufumi.openreports.gateways.filestores.s3.AwsS3Repository

import java.nio.file.Path

class StorageServiceImpl @Inject() (
    localFileRepository: LocalFileRepository,
    awsS3Repository: AwsS3Repository,
) extends StorageService {

  override def url(workspaceId: String, key: String, storageType: StorageType): String = ???

  override def get(workspaceId: String, key: String, storageType: StorageType): Path = {
    storageType match {
      case StorageTypes.Local => localFileRepository.get(workspaceId, key)
      case StorageTypes.S3    => awsS3Repository.get(workspaceId, key)
      case _                  => null
    }
  }

  override def create(
      workspaceId: String,
      key: String,
      storageType: StorageType,
      file: Path,
  ): Unit = {
    storageType match {
      case StorageTypes.Local => localFileRepository.create(workspaceId, key, file)
      case StorageTypes.S3    => awsS3Repository.create(workspaceId, key, file)
    }
  }

  override def delete(workspaceId: String, key: String, storageType: StorageType): Unit = ???
}
