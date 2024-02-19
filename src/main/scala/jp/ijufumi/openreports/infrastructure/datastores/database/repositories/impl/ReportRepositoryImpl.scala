package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import com.google.inject.Inject
import queries.{reportQuery => query, templateQuery}
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.ReportRepository
import jp.ijufumi.openreports.presentation.models.responses.Report
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await

class ReportRepositoryImpl @Inject() (db: Database) extends ReportRepository {
  override def gets(
      workspaceId: String,
      offset: Int = 0,
      limit: Int = -1,
      templateId: String = "",
  ): (Seq[Report], Int) = {
    var filtered = query.filter(_.workspaceId === workspaceId).drop(offset)
    if (templateId.nonEmpty) {
      filtered = filtered.filter(_.templateId === templateId)
    }
    val count = Await.result(db.run(query.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    (Await.result(db.run(filtered.result), queryTimeout).map(r => Report(r)), count)
  }

  override def getsWithTemplate(
      workspaceId: String,
      offset: Int = 0,
      limit: Int = -1,
      templateId: String = "",
  ): (Seq[Report], Int) = {
    var getById = query
      .filter(_.workspaceId === workspaceId)
      .join(templateQuery)
      .on(_.templateId === _.id)
      .sortBy(_._1.id)

    if (templateId.nonEmpty) {
      getById = getById.filter(_._1.templateId === templateId)
    }
    val count = Await.result(db.run(getById.length.result), queryTimeout)
    if (offset > 0) {
      getById = getById.drop(offset)
    }
    if (limit > 0) {
      getById = getById.take(limit)
    }

    (Await.result(db.run(getById.result), queryTimeout).map(r => Report(r)), count)
  }

  override def getById(workspaceId: String, id: String): Option[Report] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head).map(r => Report(r))
  }

  override def getByIdWithTemplate(
      workspaceId: String,
      id: String,
  ): Option[Report] = {
    val getById = query
      .filter(_.id === id)
      .join(templateQuery)
      .on(_.templateId === _.id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head).map(r => Report(r))
  }

  override def register(model: Report): Option[Report] = {
    val register = (query += model.toEntity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(model.workspaceId, model.id)
  }

  override def update(model: Report): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = query.insertOrUpdate(newModel.toEntity).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(workspaceId: String, id: String): Unit = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
