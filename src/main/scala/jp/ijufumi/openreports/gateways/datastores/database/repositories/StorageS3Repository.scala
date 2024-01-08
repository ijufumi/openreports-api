package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.gateways.datastores.database.entities.StorageS3

trait StorageS3Repository {
  def getById(workspaceId: String, id: String): Option[StorageS3]

  def gets(workspaceId: String): Seq[StorageS3]

  def register(storage: StorageS3): Option[StorageS3]

  def update(storage: StorageS3): Unit
}
