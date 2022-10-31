package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.services.StorageService
import jp.ijufumi.openreports.repositories.system.{AwsS3Repository, LocalFileRepository}
import jp.ijufumi.openreports.entities.enums.StorageTypes
import jp.ijufumi.openreports.entities.enums.StorageTypes.StorageType

import java.io.InputStream

class StorageServiceImpl @Inject()(
    localFileRepository: LocalFileRepository,
    awsS3Repository: AwsS3Repository,
) extends StorageService {

  override def url(workspaceId: String, key: String, storageType: StorageType): String = ???

  override def get(workspaceId: String, key: String, storageType: StorageType): InputStream = {
    if (storageType == StorageTypes.Local) {
      return localFileRepository.get(key)
    }
    null
  }

  override def create(
      workspaceId: String,
      key: String,
      storageType: StorageType,
      file: InputStream,
  ): Unit = ???

  override def delete(workspaceId: String, key: String, storageType: StorageType): Unit = ???
}
