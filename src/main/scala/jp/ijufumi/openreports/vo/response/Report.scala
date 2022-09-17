package jp.ijufumi.openreports.vo.response

import java.time.LocalDateTime
import jp.ijufumi.openreports.entities.{Report => ReportEntity, ReportTemplate}
import jp.ijufumi.openreports.utils.Dates

case class Report(
    id: String,
    name: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    reportTemplateName: String,
)

object Report {
  def apply(entity1: ReportEntity, entity2: ReportTemplate): Report = {
    Report(
      entity1.id,
      entity1.name,
      Dates.toLocalDateTime(entity1.createdAt),
      Dates.toLocalDateTime(entity1.updatedAt),
      entity2.name,
    )
  }
}
