package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.models.value.enums.StorageTypes.StorageType
import jp.ijufumi.openreports.gateways.datastores.database.entities.{Template => ReportTemplateEntity}

case class Template(
    id: String,
    name: String,
    filePath: String,
    storageType: StorageType,
    fileSize: Long,
    createdAt: Long,
    updatedAt: Long,
)

object Template {
  def apply(entity: ReportTemplateEntity): Template = {
    Template(
      entity.id,
      entity.name,
      entity.filePath,
      entity.storageType,
      entity.fileSize,
      entity.createdAt,
      entity.updatedAt,
    )
  }
}
