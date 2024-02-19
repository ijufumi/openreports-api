package jp.ijufumi.openreports.services

import java.nio.file.Path
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes

trait StorageService {
  def url(workspaceId: String, key: String, storageType: StorageTypes.StorageType): String

  def get(workspaceId: String, key: String, storageType: StorageTypes.StorageType): Path

  def create(workspaceId: String, key: String, storageType: StorageTypes.StorageType, file: Path): Unit

  def delete(workspaceId: String, key: String, storageType: StorageTypes.StorageType): Unit
}
