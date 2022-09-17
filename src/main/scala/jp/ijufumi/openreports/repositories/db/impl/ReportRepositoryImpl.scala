package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.{DataSource, Report, ReportTemplate}
import jp.ijufumi.openreports.entities.queries.{
  dataSourceQuery,
  reportQuery => query,
  reportTemplateQuery,
}
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.repositories.db.ReportRepository
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ReportRepositoryImpl @Inject() (db: Database) extends ReportRepository {
  override def gets(): Seq[Report] = {
    Await.result(db.run(query.result), Duration("10s"))
  }

  override def getsWithTemplate(): Seq[(Report, ReportTemplate, DataSource)] = {
    val getById = query
      .join(reportTemplateQuery)
      .on(_.reportTemplateId === _.id)
      .join(dataSourceQuery)
      .on(_._1.dataSourceId === _.id)
    val models = Await.result(db.run(getById.result), Duration("10s"))
    models.map(m => (m._1._1, m._1._2, m._2))
  }

  override def getById(id: String): Option[Report] = {
    val getById = query
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), Duration("10s"))
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def getWithTemplateById(id: String): Option[(Report, ReportTemplate, DataSource)] = {
    val getById = query
      .filter(_.id === id)
      .join(reportTemplateQuery)
      .on(_.reportTemplateId === _.id)
      .join(dataSourceQuery)
      .on(_._1.dataSourceId === _.id)
    val models = Await.result(db.run(getById.result), Duration("10s"))
    if (models.isEmpty) {
      return None
    }
    val result = models.head

    Some((result._1._1, result._1._2, result._2))
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
