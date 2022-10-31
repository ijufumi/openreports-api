package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.entities.enums.StorageTypes.StorageType

import java.io.File


trait OutputService {
  def output(workspaceId: String, filePath: String, storageType: StorageType, dataSourceId: String): Option[File]
}
