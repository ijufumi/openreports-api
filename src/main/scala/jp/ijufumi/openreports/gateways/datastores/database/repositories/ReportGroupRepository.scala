package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.gateways.datastores.database.entities.ReportGroup

trait ReportGroupRepository {

  def gets(
      workspaceId: String,
      offset: Int,
      limit: Int,
  ): (Seq[ReportGroup], Int)

  def getById(workspaceId: String, id: String): Option[ReportGroup]

  def register(model: ReportGroup): Option[ReportGroup]

  def update(model: ReportGroup): Unit

  def delete(workspaceId: String, id: String): Unit
}
