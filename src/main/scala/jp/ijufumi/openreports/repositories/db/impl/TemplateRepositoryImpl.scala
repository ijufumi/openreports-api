package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.Template
import jp.ijufumi.openreports.entities.queries.{templateQuery => query}
import jp.ijufumi.openreports.repositories.db.TemplateRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class TemplateRepositoryImpl @Inject()(db: Database) extends TemplateRepository {
  override def gets(workspaceId: String, offset: Int, limit: Int): (Seq[Template], Int) = {
    var filtered = query.filter(_.workspaceId === workspaceId).drop(offset)
    val count = Await.result(db.run(query.length.result), Duration("10s"))
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    (Await.result(db.run(filtered.result), Duration("10s")), count)
  }

  override def getById(workspaceId: String, id: String): Option[Template] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), Duration("10s"))
    if (models.isEmpty) {
      return None
    }
    Some(models.head)

  }

  override def register(model: Template): Option[Template] = {
    val register = (query += model).withPinnedSession
    Await.result(db.run(register), Duration("1m"))
    getById(model.workspaceId, model.id)
  }

  override def update(model: Template): Unit = {
    query.insertOrUpdate(model).withPinnedSession
  }

  override def delete(workspaceId: String, id: String): Unit = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete), Duration("10s"))
  }
}
