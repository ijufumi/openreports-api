package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.entities.{ReportGroup => ReportGroupEntity}

case class ReportGroup(id: String, name: String, createdAt: Long, updatedAt: Long)

object ReportGroup {
  def apply(entity: ReportGroupEntity): ReportGroup = {
    ReportGroup(entity.id, entity.name, entity.createdAt, entity.updatedAt)
  }
}
