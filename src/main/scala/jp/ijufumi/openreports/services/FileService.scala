package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.entities.enums.StorageTypes.StorageType

import java.io.InputStream

trait FileService {
  def url(workspaceId: String, key: String, storageType: StorageType): String

  def get(workspaceId: String, key: String, storageType: StorageType): InputStream

  def create(workspaceId: String, key: String, storageType: StorageType, file: InputStream): Unit

  def delete(workspaceId: String, key: String, storageType: StorageType): Unit
}
