package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.DataSource

trait DataSourceRepository {
  def getById(workspaceId: String, id: String): Option[DataSource]

  def getAll(workspaceId: String): Seq[DataSource]

  def getByIdWithDriverType(workspaceId: String, id: String): Option[DataSource]

  def getAllWithDriverType(workspaceId: String): Seq[DataSource]

  def register(dataSource: DataSource): Option[DataSource]

  def update(dataSource: DataSource): Unit

  def delete(workspaceId: String, id: String): Unit
}
