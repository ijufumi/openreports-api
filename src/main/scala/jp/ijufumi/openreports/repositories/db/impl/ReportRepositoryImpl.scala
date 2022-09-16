package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.{Report, ReportTemplate}
import jp.ijufumi.openreports.entities.queries.{reportQuery => query, reportTemplateQuery}
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.repositories.db.ReportRepository
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ReportRepositoryImpl @Inject() (db: Database) extends ReportRepository {
  override def getById(id: String): Option[Report] = {
    val getById = query
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), Duration("10s"))
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def getWithTemplateById(id: String): Option[(Report, ReportTemplate)] = {
    val getById = query
      .filter(_.id === id)
      .join(reportTemplateQuery)
      .on(_.reportTemplateId === _.id)
    val models = Await.result(db.run(getById.result), Duration("10s"))
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def register(model: Report): Option[Report] = {
    val register = (query += model).withPinnedSession
    Await.result(db.run(register), Duration("1m"))
    getById(model.id)
  }

  override def update(model: Report): Unit = {
    query.insertOrUpdate(model).withPinnedSession
  }
}
