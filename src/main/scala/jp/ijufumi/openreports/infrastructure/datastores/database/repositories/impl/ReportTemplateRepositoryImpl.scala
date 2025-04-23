package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.ReportTemplateRepository
import jp.ijufumi.openreports.domain.models.entity.ReportTemplate
import jp.ijufumi.openreports.domain.models.entity.ReportTemplate.conversions._
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
    var filtered = templateQuery.filter(_.workspaceId === workspaceId).drop(offset)
    val count = Await.result(db.run(filtered.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    val result = Await.result(db.run(filtered.result), queryTimeout)
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
    templateQuery.insertOrUpdate(model).withPinnedSession
  }

  override def delete(db: Database, workspaceId: String, id: String): Unit = {
    val getById = templateQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
