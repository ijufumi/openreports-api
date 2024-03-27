package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.StorageS3
import slick.jdbc.JdbcBackend.Database

trait StorageS3Repository {
  def getById(db: Database, workspaceId: String, id: String): Option[StorageS3]

  def gets(db: Database, workspaceId: String): Seq[StorageS3]

  def register(db: Database, storage: StorageS3): Option[StorageS3]

  def update(db: Database, storage: StorageS3): Unit
}
