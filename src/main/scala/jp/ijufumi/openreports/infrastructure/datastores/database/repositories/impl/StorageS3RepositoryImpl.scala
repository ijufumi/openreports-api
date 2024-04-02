package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.StorageS3Repository
import jp.ijufumi.openreports.domain.models.entity.StorageS3
import jp.ijufumi.openreports.domain.models.entity.StorageS3.conversions._
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await

class StorageS3RepositoryImpl extends StorageS3Repository {
  override def getById(db: Database, workspaceId: String, id: String): Option[StorageS3] = {
    val getById = storageQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def gets(db: Database, workspaceId: String): Seq[StorageS3] = {
    val getById = storageQuery
      .filter(_.workspaceId === workspaceId)
    val result = Await.result(db.run(getById.result), queryTimeout)
    result
  }

  override def register(db: Database, model: StorageS3): Option[StorageS3] = {
    val register = (storageQuery += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.workspaceId, model.id)
  }

  override def update(db: Database, model: StorageS3): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = storageQuery.insertOrUpdate(newModel).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }
}
