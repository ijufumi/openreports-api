package jp.ijufumi.openreports.usecase.interactor

import com.google.inject.Inject
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.domain.repository.DataSourceRepository
import jp.ijufumi.openreports.usecase.port.input.DataSourceUseCase
import jp.ijufumi.openreports.usecase.port.input.param.{
  CreateDataSourceInput,
  UpdateDataSourceInput,
}
import jp.ijufumi.openreports.utils.{IDs, Logging}
import jp.ijufumi.openreports.domain.port.ConnectionPoolPort
import jp.ijufumi.openreports.domain.models.entity.{DataSource => DataSourceModel, Lists}
import slick.jdbc.JdbcBackend.Database

import java.sql.Connection

class DataSourceInteractor @Inject() (
    db: Database,
    dataSourceRepository: DataSourceRepository,
    connectionPoolPort: ConnectionPoolPort,
) extends DataSourceUseCase
    with Logging {
  def connection(workspaceId: String, dataSourceId: String): Connection = {
    val dataSourceOpt = dataSourceRepository.getByIdWithDriverType(db, workspaceId, dataSourceId)
    if (dataSourceOpt.isEmpty) {
      throw new NotFoundException()
    }
    val dataSource = dataSourceOpt.get

    connectionPoolPort.newConnection(
      dataSource.name,
      dataSource.username,
      dataSource.password,
      dataSource.url,
      dataSource.driverType.get.jdbcDriverClass,
      dataSource.maxPoolSize,
    )
  }

  override def getDataSources(workspaceId: String): Lists[DataSourceModel] = {
    val dataSources = dataSourceRepository.getAllWithDriverType(db, workspaceId)
    Lists(
      dataSources,
      0,
      dataSources.size,
      dataSources.size,
    )
  }

  override def getDataSource(workspaceId: String, id: String): Option[DataSourceModel] = {
    dataSourceRepository.getByIdWithDriverType(db, workspaceId, id)
  }

  override def registerDataSource(
      workspaceId: String,
      requestVal: CreateDataSourceInput,
  ): Option[DataSourceModel] = {
    val dataSource = DataSourceModel(
      IDs.ulid(),
      requestVal.name,
      requestVal.url,
      requestVal.username,
      requestVal.password,
      requestVal.driverTypeId,
      DataSourceModel.maxPoolSize,
      workspaceId,
    )
    dataSourceRepository.register(db, dataSource)
    getDataSource(workspaceId, dataSource.id)
  }

  override def updateDataSource(
      workspaceId: String,
      id: String,
      requestVal: UpdateDataSourceInput,
  ): Option[DataSourceModel] = {
    val dataSource = dataSourceRepository.getById(db, workspaceId, id)
    if (dataSource.isEmpty) {
      return None
    }
    val newDataSource = dataSource.get.copy(name = requestVal.name)
    dataSourceRepository.update(db, newDataSource)
    getDataSource(workspaceId, newDataSource.id)
  }

  override def deleteDataSource(workspaceId: String, id: String): Unit = {
    dataSourceRepository.delete(db, workspaceId, id)
  }
}
