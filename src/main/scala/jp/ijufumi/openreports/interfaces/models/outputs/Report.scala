package jp.ijufumi.openreports.interfaces.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{
  Report => ReportEntity,
  Template => ReportTemplateEntity,
}
import jp.ijufumi.openreports.interfaces.models.inputs.UpdateReport
import jp.ijufumi.openreports.utils.Dates

case class Report(
    id: String,
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    reportTemplate: Option[Template] = None,
) {
  def toEntity: ReportEntity = {
    ReportEntity(
      this.id,
      this.name,
      this.templateId,
      this.dataSourceId,
      this.workspaceId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def copyForUpdate(input: UpdateReport): Report = {
    this.copy(name = input.name, templateId = input.templateId, dataSourceId = input.dataSourceId)
  }
}

object Report {
  def apply(entity: ReportEntity): Report = {
    Report(
      entity.id,
      entity.name,
      entity.templateId,
      entity.dataSourceId,
      entity.workspaceId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
  def apply(entity: (ReportEntity, ReportTemplateEntity)): Report = {
    Report(
      entity._1.id,
      entity._1.name,
      entity._1.templateId,
      entity._1.dataSourceId,
      entity._1.workspaceId,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      Some(Template(entity._2)),
    )
  }
}
