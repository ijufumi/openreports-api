package jp.ijufumi.openreports.interfaces.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{
  ReportGroup => ReportGroupEntity,
}
import jp.ijufumi.openreports.interfaces.models.inputs.UpdateReportGroup
import jp.ijufumi.openreports.utils.Dates

case class ReportGroup(
    id: String,
    name: String,
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: ReportGroupEntity = {
    ReportGroupEntity(
      this.id,
      this.name,
      this.workspaceId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def copyForUpdate(input: UpdateReportGroup): ReportGroup = {
    this.copy(name = input.name)
  }
}

object ReportGroup {
  def apply(entity: ReportGroupEntity): ReportGroup = {
    ReportGroup(
      entity.id,
      entity.name,
      entity.workspaceId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
}
