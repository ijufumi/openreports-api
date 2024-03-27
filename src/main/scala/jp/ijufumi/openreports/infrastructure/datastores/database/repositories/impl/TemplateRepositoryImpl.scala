package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import queries.{templateQuery => query}
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.TemplateRepository
import jp.ijufumi.openreports.domain.models.entity.Template
import jp.ijufumi.openreports.domain.models.entity.Template.conversions._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class TemplateRepositoryImpl extends TemplateRepository {
  override def gets(
      db: Database,
      workspaceId: String,
      offset: Int,
      limit: Int,
  ): (Seq[Template], Int) = {
    var filtered = query.filter(_.workspaceId === workspaceId).drop(offset)
    val count = Await.result(db.run(query.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    val result = Await.result(db.run(filtered.result), queryTimeout)
    (result, count)
  }

  override def getById(db: Database, workspaceId: String, id: String): Option[Template] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)

  }

  override def register(db: Database, model: Template): Option[Template] = {
    val register = (query += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.workspaceId, model.id)
  }

  override def update(db: Database, model: Template): Unit = {
    query.insertOrUpdate(model).withPinnedSession
  }

  override def delete(db: Database, workspaceId: String, id: String): Unit = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
