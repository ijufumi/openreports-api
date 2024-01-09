package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.gateways.datastores.database.entities.enums.StorageTypes.StorageType

import java.io.File


trait OutputService {
  def output(workspaceId: String, filePath: String, storageType: StorageType, dataSourceId: Option[String]): Option[File]
}
