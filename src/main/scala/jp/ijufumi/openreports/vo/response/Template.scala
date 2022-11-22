package jp.ijufumi.openreports.vo.response

import jp.ijufumi.openreports.entities.enums.StorageTypes.StorageType
import jp.ijufumi.openreports.entities.{Template => ReportTemplateEntity}

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
