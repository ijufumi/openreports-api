package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.ReportParameterRepository
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl.queries.{
  reportParameterQuery => query,
}
import jp.ijufumi.openreports.domain.models.entity.ReportParameter
import jp.ijufumi.openreports.domain.models.entity.ReportParameter.conversions._
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
    var filtered = query.filter(_.workspaceId === workspaceId).drop(offset)
    val count = Await.result(db.run(query.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    val result = Await.result(db.run(filtered.result), queryTimeout)
    (result, count)
  }

  override def getById(db: Database, workspaceId: String, id: String): Option[ReportParameter] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def register(db: Database, model: ReportParameter): Option[ReportParameter] = {
    val register = (query += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.workspaceId, model.id)
  }

  override def update(db: Database, model: ReportParameter): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = query.insertOrUpdate(newModel).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(db: Database, workspaceId: String, id: String): Unit = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
