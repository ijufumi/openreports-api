package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.Storage

trait StorageRepository {
  def getById(id: String): Option[Storage]

  def getAll: Seq[Storage]

  def register(storage: Storage): Option[Storage]

  def update(storage: Storage): Unit
}
