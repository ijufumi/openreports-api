package jp.ijufumi.openreports.usecase.port.input

import java.io.File
import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes

trait OutputUseCase {
  def output(workspaceId: String, filePath: String, storageType: StorageTypes.StorageType, dataSourceId: Option[String], asPDF: Boolean): Option[File]
}
