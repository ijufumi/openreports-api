package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.ReportRepository
import jp.ijufumi.openreports.domain.models.entity.Report
import jp.ijufumi.openreports.domain.models.entity.Report.conversions._
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await

class ReportRepositoryImpl extends ReportRepository {
  override def gets(
      db: Database,
      workspaceId: String,
      offset: Int = 0,
      limit: Int = -1,
      templateId: String = "",
  ): (Seq[Report], Int) = {
    var filtered = reportQuery.filter(_.workspaceId === workspaceId).drop(offset)
    if (templateId.nonEmpty) {
      filtered = filtered.filter(_.templateId === templateId)
    }
    val count = Await.result(db.run(filtered.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    val result = Await.result(db.run(filtered.result), queryTimeout)
    (result, count)
  }

  override def getsWithTemplate(
      db: Database,
      workspaceId: String,
      offset: Int = 0,
      limit: Int = -1,
      templateId: String = "",
  ): (Seq[Report], Int) = {
    var getById = reportQuery
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

    val result = Await.result(db.run(getById.result), queryTimeout)
    (result, count)
  }

  override def getById(db: Database, workspaceId: String, id: String): Option[Report] = {
    val getById = reportQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def getByIdWithTemplate(
      db: Database,
      workspaceId: String,
      id: String,
  ): Option[Report] = {
    val getById = reportQuery
      .filter(_.id === id)
      .join(templateQuery)
      .on(_.templateId === _.id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def register(db: Database, model: Report): Option[Report] = {
    val register = (reportQuery += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.workspaceId, model.id)
  }

  override def update(db: Database, model: Report): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = reportQuery.insertOrUpdate(newModel).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(db: Database, workspaceId: String, id: String): Unit = {
    val getById = reportQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
