package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.ReportParameterRepository
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl.queries.{
  reportParameterQuery => query,
}
import jp.ijufumi.openreports.presentation.models.responses.ReportParameter
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class ReportParameterRepositoryImpl @Inject() (db: Database) extends ReportParameterRepository {
  override def gets(
      workspaceId: String,
      offset: Int = 0,
      limit: Int = -1,
  ): (Seq[ReportParameter], Int) = {
    var filtered = query.filter(_.workspaceId === workspaceId).drop(offset)
    val count = Await.result(db.run(query.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    (Await.result(db.run(filtered.result), queryTimeout).map(r => ReportParameter(r)), count)
  }

  override def getById(workspaceId: String, id: String): Option[ReportParameter] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head).map(r => ReportParameter(r))
  }

  override def register(model: ReportParameter): Option[ReportParameter] = {
    val register = (query += model.toEntity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(model.workspaceId, model.id)
  }

  override def update(model: ReportParameter): Unit = {
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
