package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.ReportGroup
import slick.jdbc.JdbcBackend.Database

trait ReportGroupRepository {

  def gets(
      db: Database,
      workspaceId: String,
      offset: Int,
      limit: Int,
  ): (Seq[ReportGroup], Int)

  def getById(db: Database, workspaceId: String, id: String): Option[ReportGroup]

  def register(db: Database, model: ReportGroup): Option[ReportGroup]

  def update(db: Database, model: ReportGroup): Unit

  def delete(db: Database, workspaceId: String, id: String): Unit
}
