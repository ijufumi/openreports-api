package jp.ijufumi.openreports.repositories.db.impl

import jp.ijufumi.openreports.entities.Storage
import jp.ijufumi.openreports.repositories.db.StorageRepository

class StorageRepositoryImpl extends StorageRepository {
  override def getById(id: String): Option[Storage] = ???

  override def getAll: Seq[Storage] = ???

  override def register(storage: Storage): Option[Storage] = ???

  override def update(storage: Storage): Unit = ???
}
