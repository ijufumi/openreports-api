package jp.ijufumi.openreports.usecase.interactor

import com.google.inject.Inject
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.usecase.port.input.StorageUseCase
import jp.ijufumi.openreports.infrastructure.storage.local.{
  LocalFileRepository,
  LocalSeedFileRepository,
}
import jp.ijufumi.openreports.infrastructure.storage.s3.AwsS3Repository

import java.nio.file.Path

class StorageInteractor @Inject() (
    localFileRepository: LocalFileRepository,
    localSeedFileRepository: LocalSeedFileRepository,
    awsS3Repository: AwsS3Repository,
) extends StorageUseCase {

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
      case StorageTypes.Local     => localFileRepository.get(workspaceId, key)
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
      case StorageTypes.Local     => localFileRepository.create(workspaceId, key, file)
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
