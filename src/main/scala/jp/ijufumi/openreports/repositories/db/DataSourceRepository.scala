package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.DataSource

trait DataSourceRepository {
  def getById(id: String): Option[DataSource]

  def getAll(): Seq[DataSource]

  def register(dataSource: DataSource): Option[DataSource]

  def update(dataSource: DataSource): Unit
}
