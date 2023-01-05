package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.{DataSource, DriverType}

trait DataSourceRepository {
  def getById(workspaceId: String, id: String): Option[(DataSource, DriverType)]

  def getAll(workspaceId: String): Seq[(DataSource, DriverType)]

  def register(dataSource: DataSource): Option[(DataSource, DriverType)]

  def update(dataSource: DataSource): Unit
}
