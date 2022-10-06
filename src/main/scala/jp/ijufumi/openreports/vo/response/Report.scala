package jp.ijufumi.openreports.vo.response

import jp.ijufumi.openreports.entities.{Report => ReportEntity, ReportTemplate}

case class Report(
    id: String,
    name: String,
    createdAt: Long,
    updatedAt: Long,
    reportTemplateName: String,
)

object Report {
  def apply(entity1: ReportEntity, entity2: ReportTemplate): Report = {
    Report(
      entity1.id,
      entity1.name,
      entity1.createdAt,
      entity1.updatedAt,
      entity2.name,
    )
  }
}
