package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{ReportGroup => ReportGroupEntity}
import jp.ijufumi.openreports.presentation.models.requests.UpdateReportGroup
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
