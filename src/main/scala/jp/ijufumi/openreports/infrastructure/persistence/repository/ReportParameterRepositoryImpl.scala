package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.ReportParameterRepository
import jp.ijufumi.openreports.domain.models.entity.ReportParameter
import jp.ijufumi.openreports.infrastructure.persistence.converter.ReportParameterConverter.conversions._
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class ReportParameterRepositoryImpl extends ReportParameterRepository {
  override def gets(
      db: Database,
      workspaceId: String,
      offset: Int = 0,
      limit: Int = -1,
  ): (Seq[ReportParameter], Int) = {
    val filtered = reportParameterQuery.filter(_.workspaceId === workspaceId)
    val count = Await.result(db.run(filtered.length.result), queryTimeout)
    val withOffset = if (offset > 0) filtered.drop(offset) else filtered
    val paged = if (limit > 0) withOffset.take(limit) else withOffset
    val result = Await.result(db.run(paged.result), queryTimeout)
    (result, count)
  }

  override def getById(db: Database, workspaceId: String, id: String): Option[ReportParameter] = {
    val getById = reportParameterQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def register(db: Database, model: ReportParameter): Option[ReportParameter] = {
    val register = (reportParameterQuery += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.workspaceId, model.id)
  }

  override def update(db: Database, model: ReportParameter): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = reportParameterQuery.insertOrUpdate(newModel).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(db: Database, workspaceId: String, id: String): Unit = {
    val getById = reportParameterQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
