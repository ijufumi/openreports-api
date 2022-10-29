package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.{DataSource, DriverType}

trait DataSourceRepository {
  def getById(id: String): Option[(DataSource, DriverType)]

  def getAll: Seq[DataSource]

  def register(dataSource: DataSource): Option[(DataSource, DriverType)]

  def update(dataSource: DataSource): Unit
}
