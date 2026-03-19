package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.usecase.port.input.param.{CreateDataSourceInput, UpdateDataSourceInput}
import jp.ijufumi.openreports.domain.models.entity.{DataSource, Lists}

import java.sql.Connection

trait DataSourceUseCase {
  def connection(workspaceId: String, dataSourceId: String): Connection

  def getDataSources(workspaceId: String): Lists[DataSource]

  def getDataSource(workspaceId: String, id: String): Option[DataSource]

  def registerDataSource(workspaceId: String, requestVal: CreateDataSourceInput): Option[DataSource]

  def updateDataSource(workspaceId: String, id: String, updateDataSource: UpdateDataSourceInput): Option[DataSource]

  def deleteDataSource(workspaceId: String, id: String): Unit
}
