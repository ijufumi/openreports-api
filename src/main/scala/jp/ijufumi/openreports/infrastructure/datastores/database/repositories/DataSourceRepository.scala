package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.DataSource
import slick.jdbc.JdbcBackend.Database

trait DataSourceRepository {
  def getById(db: Database, workspaceId: String, id: String): Option[DataSource]

  def getAll(db: Database, workspaceId: String): Seq[DataSource]

  def getByIdWithDriverType(db: Database, workspaceId: String, id: String): Option[DataSource]

  def getAllWithDriverType(db: Database, workspaceId: String): Seq[DataSource]

  def register(db: Database, dataSource: DataSource): Option[DataSource]

  def update(db: Database, dataSource: DataSource): Unit

  def delete(db: Database, workspaceId: String, id: String): Unit
}
