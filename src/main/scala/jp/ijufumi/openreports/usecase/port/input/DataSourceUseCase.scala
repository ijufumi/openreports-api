package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.presentation.request.{CreateDataSource, UpdateDataSource}
import jp.ijufumi.openreports.presentation.response.{DataSource, Lists}

import java.sql.Connection

trait DataSourceUseCase {
  def connection(workspaceId: String, dataSourceId: String): Connection

  def getDataSources(workspaceId: String): Lists[DataSource]

  def getDataSource(workspaceId: String, id: String): Option[DataSource]

  def registerDataSource(workspaceId: String, requestVal: CreateDataSource): Option[DataSource]

  def updateDataSource(workspaceId: String, id: String, updateDataSource: UpdateDataSource): Option[DataSource]

  def deleteDataSource(workspaceId: String, id: String): Unit
}
