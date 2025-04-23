package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.ReportTemplate
import slick.jdbc.JdbcBackend.Database

trait ReportTemplateRepository {

  def gets(db: Database, workspaceId: String, offset: Int, limit: Int): (Seq[ReportTemplate], Int)

  def getById(db: Database, workspaceId: String, id: String): Option[ReportTemplate]

  def register(db: Database, model: ReportTemplate): Option[ReportTemplate]

  def update(db: Database, model: ReportTemplate): Unit

  def delete(db: Database, workspaceId: String, id: String): Unit
}
