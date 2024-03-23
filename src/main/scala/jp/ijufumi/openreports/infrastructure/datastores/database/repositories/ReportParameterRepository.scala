package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.ReportParameter
import slick.jdbc.JdbcBackend.Database

trait ReportParameterRepository {

  def gets(
      db: Database,
      workspaceId: String,
      offset: Int,
      limit: Int,
  ): (Seq[ReportParameter], Int)

  def getById(db: Database, workspaceId: String, id: String): Option[ReportParameter]

  def register(db: Database, model: ReportParameter): Option[ReportParameter]

  def update(db: Database, model: ReportParameter): Unit

  def delete(db: Database, workspaceId: String, id: String): Unit
}
