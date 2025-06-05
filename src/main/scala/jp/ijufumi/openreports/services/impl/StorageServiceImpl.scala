package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.services.StorageService
import jp.ijufumi.openreports.infrastructure.filestores.local.{
  LocalFileRepository,
  LocalSeedFileRepository,
  LocalStackS3Repository,
}
import jp.ijufumi.openreports.infrastructure.filestores.s3.AwsS3Repository

import java.nio.file.Path

class StorageServiceImpl @Inject() (
    localFileRepository: LocalFileRepository,
    localSeedFileRepository: LocalSeedFileRepository,
    localStackS3Repository: LocalStackS3Repository,
    awsS3Repository: AwsS3Repository,
) extends StorageService {

  override def url(
      workspaceId: String,
      key: String,
      storageType: StorageTypes.StorageType,
  ): String = ???

  override def get(
      workspaceId: String,
      key: String,
      storageType: StorageTypes.StorageType,
  ): Path = {
    storageType match {
      case StorageTypes.Local => {
        if (Config.useLocalStackAsLocalStorage()) {
          localStackS3Repository.get(workspaceId, key)
        } else {
          localFileRepository.get(workspaceId, key)
        }
      }
      case StorageTypes.LocalSeed => localSeedFileRepository.get(workspaceId, key)
      case StorageTypes.S3        => awsS3Repository.get(workspaceId, key)
      case _                      => null
    }
  }

  override def create(
      workspaceId: String,
      key: String,
      storageType: StorageTypes.StorageType,
      file: Path,
  ): Unit = {
    storageType match {
      case StorageTypes.Local => {
        if (Config.useLocalStackAsLocalStorage()) {
          localStackS3Repository.create(workspaceId, key, file)
        } else {
          localFileRepository.create(workspaceId, key, file)
        }
      }
      case StorageTypes.LocalSeed => localSeedFileRepository.create(workspaceId, key, file)
      case StorageTypes.S3        => awsS3Repository.create(workspaceId, key, file)
    }
  }

  override def delete(
      workspaceId: String,
      key: String,
      storageType: StorageTypes.StorageType,
  ): Unit = ???
}
