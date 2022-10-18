package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.ReportTemplate
import jp.ijufumi.openreports.entities.queries.{reportTemplateQuery => query}
import jp.ijufumi.openreports.repositories.db.ReportTemplateRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ReportTemplateRepositoryImpl @Inject() (db: Database) extends ReportTemplateRepository {
  override def gets(offset: Int, limit: Int): (Seq[ReportTemplate], Int) = {
    var filtered = query.drop(offset)
    val count = Await.result(db.run(query.length.result), Duration("10s"))
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    (Await.result(db.run(filtered.result), Duration("10s")), count)
  }

  override def getById(id: String): Option[ReportTemplate] = {
    val getById = query
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), Duration("10s"))
    if (models.isEmpty) {
      return None
    }
    Some(models.head)

  }

  override def register(model: ReportTemplate): Option[ReportTemplate] = {
    val register = (query += model).withPinnedSession
    Await.result(db.run(register), Duration("1m"))
    getById(model.id)
  }

  override def update(model: ReportTemplate): Unit = {
    query.insertOrUpdate(model).withPinnedSession
  }
}
