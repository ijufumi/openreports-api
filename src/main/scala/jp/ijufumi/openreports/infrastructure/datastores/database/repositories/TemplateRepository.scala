package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Template
import slick.jdbc.JdbcBackend.Database

trait TemplateRepository {

  def gets(db: Database, workspaceId: String, offset: Int, limit: Int): (Seq[Template], Int)

  def getById(db: Database, workspaceId: String, id: String): Option[Template]

  def register(db: Database, model: Template): Option[Template]

  def update(db: Database, model: Template): Unit

  def delete(db: Database, workspaceId: String, id: String): Unit
}
