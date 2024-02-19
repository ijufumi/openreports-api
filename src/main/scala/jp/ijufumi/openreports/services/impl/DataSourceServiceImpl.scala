package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.DataSourceRepository
import jp.ijufumi.openreports.services.DataSourceService
import jp.ijufumi.openreports.utils.IDs
import jp.ijufumi.openreports.infrastructure.datastores.database.pool.ConnectionPool
import jp.ijufumi.openreports.presentation.models.requests.{CreateDataSource, UpdateDataSource}
import jp.ijufumi.openreports.presentation.models.responses.{DataSource, Lists}
import jp.ijufumi.openreports.domain.models.entity.{DataSource => DataSourceModel}

import java.sql.Connection

class DataSourceServiceImpl @Inject() (dataSourceRepository: DataSourceRepository)
    extends DataSourceService {
  def connection(workspaceId: String, dataSourceId: String): Connection = {
    val dataSourceOpt = dataSourceRepository.getByIdWithDriverType(workspaceId, dataSourceId)
    if (dataSourceOpt.isEmpty) {
      throw new NotFoundException()
    }
    val dataSource = dataSourceOpt.get
    ConnectionPool.newConnection(
      dataSource.name,
      dataSource.username,
      dataSource.password,
      dataSource.url,
      dataSource.driverType.get.jdbcDriverClass,
    )
  }

  override def getDataSources(workspaceId: String): Lists[DataSource] = {
    val dataSources = dataSourceRepository.getAllWithDriverType(workspaceId)
    Lists(
      dataSources.map(d => DataSource(d)),
      0,
      dataSources.size,
      dataSources.size,
    )
  }

  override def getDataSource(workspaceId: String, id: String): Option[DataSource] = {
    dataSourceRepository.getByIdWithDriverType(workspaceId, id).map(d => DataSource(d))
  }

  override def registerDataSource(
      workspaceId: String,
      requestVal: CreateDataSource,
  ): Option[DataSource] = {
    val dataSource = DataSourceModel(
      IDs.ulid(),
      requestVal.name,
      requestVal.url,
      requestVal.username,
      requestVal.password,
      requestVal.driverTypeId,
      workspaceId,
    )
    dataSourceRepository.register(dataSource)
    getDataSource(workspaceId, dataSource.id)
  }

  override def updateDataSource(
      workspaceId: String,
      id: String,
      requestVal: UpdateDataSource,
  ): Option[DataSource] = {
    val dataSource = dataSourceRepository.getById(workspaceId, id)
    if (dataSource.isEmpty) {
      return None
    }
    val newDataSource = dataSource.get.copyForUpdate(requestVal)
    dataSourceRepository.update(newDataSource)
    getDataSource(workspaceId, newDataSource.id)
  }

  override def deleteDataSource(workspaceId: String, id: String): Unit = {
    dataSourceRepository.delete(workspaceId, id)
  }
}
