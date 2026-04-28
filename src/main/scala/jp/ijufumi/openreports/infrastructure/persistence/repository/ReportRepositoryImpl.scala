package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.ReportRepository
import jp.ijufumi.openreports.domain.models.entity.Report
import jp.ijufumi.openreports.infrastructure.persistence.converter.ReportConverter.conversions._
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
    val base = reportQuery.filter(_.workspaceId === workspaceId)
    val filtered = if (templateId.nonEmpty) base.filter(_.reportTemplateId === templateId) else base
    val count = Await.result(db.run(filtered.length.result), queryTimeout)
    val withOffset = if (offset > 0) filtered.drop(offset) else filtered
    val paged = if (limit > 0) withOffset.take(limit) else withOffset
    val result = Await.result(db.run(paged.result), queryTimeout)
    (result, count)
  }

  override def getsWithTemplate(
      db: Database,
      workspaceId: String,
      offset: Int = 0,
      limit: Int = -1,
      templateId: String = "",
  ): (Seq[Report], Int) = {
    val base = reportQuery
      .filter(_.workspaceId === workspaceId)
      .join(templateQuery)
      .on(_.reportTemplateId === _.id)
      .sortBy(_._1.id)
    val filtered =
      if (templateId.nonEmpty) base.filter(_._1.reportTemplateId === templateId) else base
    val count = Await.result(db.run(filtered.length.result), queryTimeout)
    val withOffset = if (offset > 0) filtered.drop(offset) else filtered
    val paged = if (limit > 0) withOffset.take(limit) else withOffset
    val result = Await.result(db.run(paged.result), queryTimeout)
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
      .on(_.reportTemplateId === _.id)
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
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
