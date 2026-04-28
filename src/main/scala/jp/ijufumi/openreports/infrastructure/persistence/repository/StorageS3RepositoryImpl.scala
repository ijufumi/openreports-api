package jp.ijufumi.openreports.infrastructure.persistence.repository

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.domain.repository.StorageS3Repository
import jp.ijufumi.openreports.domain.models.entity.StorageS3
import jp.ijufumi.openreports.exceptions.OptimisticLockException
import jp.ijufumi.openreports.infrastructure.persistence.converter.StorageS3Converter.conversions._
import jp.ijufumi.openreports.infrastructure.persistence.entity.{StorageS3 => StorageS3Entity}
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
    val newEntity: StorageS3Entity =
      model.copy(updatedAt = Dates.currentTimestamp(), versions = model.versions + 1)
    val q = storageQuery.filter(_.id === model.id).filter(_.versions === model.versions)
    val affected = Await.result(db.run(q.update(newEntity).withPinnedSession), queryTimeout)
    if (affected == 0) {
      throw new OptimisticLockException(
        s"StorageS3 id=${model.id} was modified concurrently or does not exist",
      )
    }
  }
}
