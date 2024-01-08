package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{
  Report => ReportEntity,
  Template => ReportTemplateEntity,
}

case class Report(
    id: String,
    name: String,
    createdAt: Long,
    updatedAt: Long,
    reportTemplateId: String,
    reportTemplateName: String,
)

object Report {
  def apply(entity: Tuple2[ReportEntity, ReportTemplateEntity]): Report = {
    Report(
      entity._1.id,
      entity._1.name,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._2.id,
      entity._2.name,
    )
  }
}
