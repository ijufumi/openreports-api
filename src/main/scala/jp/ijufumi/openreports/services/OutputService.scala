package jp.ijufumi.openreports.services

import java.io.File
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes

trait OutputService {
  def output(workspaceId: String, filePath: String, storageType: StorageTypes.StorageType, dataSourceId: Option[String], asPDF: Boolean): Option[File]
}
