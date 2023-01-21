package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.models.inputs.{CreateDataSource, UpdateDataSource}

import java.sql.Connection
import jp.ijufumi.openreports.models.outputs.{DataSource, Lists}

trait DataSourceService {
  def connection(workspaceId: String, dataSourceId: String): Connection

  def getDataSources(workspaceId: String): Lists[DataSource]

  def getDataSource(workspaceId: String, id: String): Option[DataSource]

  def registerDataSource(workspaceId: String, requestVal: CreateDataSource): Option[DataSource]

  def updateDataSource(workspaceId: String, id: String, updateDataSource: UpdateDataSource): Option[DataSource]

  def deleteDataSource(workspaceId: String, id: String): Unit
}
