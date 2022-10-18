package jp.ijufumi.openreports.vo.response

import jp.ijufumi.openreports.entities.{ReportTemplate => ReportTemplateEntity}

case class ReportTemplate(
    id: String,
    name: String,
    filePath: String,
    createdAt: Long,
    updatedAt: Long,
)

object ReportTemplate {
  def apply(entity: ReportTemplateEntity): ReportTemplate = {
    ReportTemplate(entity.id, entity.name, entity.filePath, entity.createdAt, entity.updatedAt)
  }
}
