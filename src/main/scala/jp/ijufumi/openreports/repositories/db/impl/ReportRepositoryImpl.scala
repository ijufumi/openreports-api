package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.{Report, ReportTemplate}
import jp.ijufumi.openreports.entities.queries.{reportQuery => query, reportTemplateQuery}
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.repositories.db.ReportRepository
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ReportRepositoryImpl @Inject() (db: Database) extends ReportRepository {
  override def gets(workspaceId: String, offset: Int = 0, limit: Int = -1): (Seq[Report], Int) = {
    var filtered = query.filter(_.workspaceId === workspaceId).drop(offset)
    val count = Await.result(db.run(query.length.result), Duration("10s"))
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    (Await.result(db.run(filtered.result), Duration("10s")), count)
  }

  override def getsWithTemplate(
      workspaceId: String,
      offset: Int = 0,
      limit: Int = -1,
  ): (Seq[(Report, ReportTemplate)], Int) = {
    var getById = query
      .filter(_.workspaceId === workspaceId)
      .join(reportTemplateQuery)
      .on(_.reportTemplateId === _.id)
      .sortBy(_._1.id)

    val count = Await.result(db.run(getById.length.result), Duration("10s"))
    if (offset > 0) {
      getById = getById.drop(offset)
    }
    if (limit > 0) {
      getById = getById.take(limit)
    }

    (Await.result(db.run(getById.result), Duration("10s")), count)
  }

  override def getById(workspaceId: String, id: String): Option[Report] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), Duration("10s"))
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def getByIdWithTemplate(
      workspaceId: String,
      id: String,
  ): Option[(Report, ReportTemplate)] = {
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
    getById(model.workspaceId, model.id)
  }

  override def update(model: Report): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = query.insertOrUpdate(newModel).withPinnedSession
    Await.result(db.run(updateQuery), Duration("1m"))
  }

  override def delete(workspaceId: String, id: String): Unit = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete), Duration("10s"))
  }
}
