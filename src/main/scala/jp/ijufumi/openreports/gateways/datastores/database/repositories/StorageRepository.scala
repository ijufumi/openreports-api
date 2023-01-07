package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.entities.Storage

trait StorageRepository {
  def getById(workspaceId: String, id: String): Option[Storage]

  def gets(workspaceId: String): Seq[Storage]

  def register(storage: Storage): Option[Storage]

  def update(storage: Storage): Unit
}
