package jp.ijufumi.openreports.services

import java.io.File


trait OutputService {
  def output(workspaceId: String, filePath: String, storageType: StorageType, dataSourceId: Option[String]): Option[File]
}
