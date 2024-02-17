package jp.ijufumi.openreports.interfaces.models.outputs

import jp.ijufumi.openreports.interfaces.models.value.enums.StorageTypes.StorageType
import jp.ijufumi.openreports.gateways.datastores.database.entities.{
  Template => ReportTemplateEntity,
}
import jp.ijufumi.openreports.interfaces.models.inputs.UpdateTemplate
import jp.ijufumi.openreports.utils.Dates

case class Template(
    id: String,
    name: String,
    filePath: String,
    workspaceId: String,
    storageType: StorageType,
    fileSize: Long,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: ReportTemplateEntity = {
    ReportTemplateEntity(
      this.id,
      this.name,
      this.filePath,
      this.workspaceId,
      this.storageType,
      this.fileSize,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def copyForUpdate(input: UpdateTemplate): Template = {
    this.copy(name = input.name)
  }
}

object Template {
  def apply(entity: ReportTemplateEntity): Template = {
    Template(
      entity.id,
      entity.name,
      entity.filePath,
      entity.workspaceId,
      entity.storageType,
      entity.fileSize,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
}
