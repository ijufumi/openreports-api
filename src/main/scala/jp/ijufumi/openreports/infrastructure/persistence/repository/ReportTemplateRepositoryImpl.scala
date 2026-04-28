package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.ReportTemplateRepository
import jp.ijufumi.openreports.domain.models.entity.ReportTemplate
import jp.ijufumi.openreports.infrastructure.persistence.converter.ReportTemplateConverter.conversions._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class ReportTemplateRepositoryImpl extends ReportTemplateRepository {
  override def gets(
      db: Database,
      workspaceId: String,
      offset: Int,
      limit: Int,
  ): (Seq[ReportTemplate], Int) = {
    val filtered = templateQuery.filter(_.workspaceId === workspaceId)
    val count = Await.result(db.run(filtered.length.result), queryTimeout)
    val withOffset = if (offset > 0) filtered.drop(offset) else filtered
    val paged = if (limit > 0) withOffset.take(limit) else withOffset
    val result = Await.result(db.run(paged.result), queryTimeout)
    (result, count)
  }

  override def getById(db: Database, workspaceId: String, id: String): Option[ReportTemplate] = {
    val getById = templateQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)

  }

  override def register(db: Database, model: ReportTemplate): Option[ReportTemplate] = {
    val register = (templateQuery += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.workspaceId, model.id)
  }

  override def update(db: Database, model: ReportTemplate): Unit = {
    val updateQuery = templateQuery.insertOrUpdate(model).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(db: Database, workspaceId: String, id: String): Unit = {
    val getById = templateQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
