package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.entities.Storage
import jp.ijufumi.openreports.entities.queries.{storageQuery => query}
import jp.ijufumi.openreports.gateways.datastores.database.repositories.StorageRepository
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class StorageRepositoryImpl @Inject() (db: Database) extends StorageRepository {
  override def getById(workspaceId: String, id: String): Option[Storage] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def gets(workspaceId: String): Seq[Storage] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
    Await.result(db.run(getById.result), queryTimeout)
  }

  override def register(model: Storage): Option[Storage] = {
    val register = (query += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(model.workspaceId, model.id)
  }

  override def update(model: Storage): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = query.insertOrUpdate(newModel).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }
}
