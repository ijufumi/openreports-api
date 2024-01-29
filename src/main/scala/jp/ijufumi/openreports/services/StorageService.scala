package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.models.value.enums.StorageTypes.StorageType

import java.nio.file.Path

trait StorageService {
  def url(workspaceId: String, key: String, storageType: StorageType): String

  def get(workspaceId: String, key: String, storageType: StorageType): Path

  def create(workspaceId: String, key: String, storageType: StorageType, file: Path): Unit

  def delete(workspaceId: String, key: String, storageType: StorageType): Unit
}
