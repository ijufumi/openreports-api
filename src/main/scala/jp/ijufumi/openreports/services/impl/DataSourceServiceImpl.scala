package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.pool.HikariPool
import jp.ijufumi.openreports.entities.DataSource
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.gateways.datastores.database.repositories.DataSourceRepository
import jp.ijufumi.openreports.services.DataSourceService
import jp.ijufumi.openreports.models.inputs.{CreateDataSource, UpdateDataSource}
import jp.ijufumi.openreports.models.outputs.{DataSource => DataSourceResponse}
import jp.ijufumi.openreports.utils.IDs

import java.sql.Connection
import scala.collection.mutable

class DataSourceServiceImpl @Inject() (dataSourceRepository: DataSourceRepository)
    extends DataSourceService {
  def connection(workspaceId: String, dataSourceId: String): Connection = {
    val dataSourceOpt = dataSourceRepository.getByIdWithDriverType(workspaceId, dataSourceId)
    if (dataSourceOpt.isEmpty) {
      throw new NotFoundException()
    }
    val (dataSource, driverType) = dataSourceOpt.get
    if (!dataSourcePool.has(dataSource.name)) {
      val config = new HikariConfig()
      config.setUsername(dataSource.username)
      config.setPassword(dataSource.password)
      config.setJdbcUrl(dataSource.url)
      config.setAutoCommit(false)
      config.setDriverClassName(driverType.jdbcDriverClass)
      dataSourcePool.add(dataSource.name, config)
    }
    dataSourcePool.connection(dataSource.name).get
  }

  override def getDataSources(workspaceId: String): Seq[DataSourceResponse] = {
    val dataSources = dataSourceRepository.getAll(workspaceId)
    dataSources.map(d => DataSourceResponse(d))
  }

  override def getDataSource(workspaceId: String, id: String): Option[DataSourceResponse] = {
    val dataSource = dataSourceRepository.getById(workspaceId, id)
    if (dataSource.isEmpty) {
      return None
    }
    Some(DataSourceResponse(dataSource.get))
  }

  override def registerDataSource(
      workspaceId: String,
      requestVal: CreateDataSource,
  ): Option[DataSourceResponse] = {
    val dataSource = DataSource(
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
  ): Option[DataSourceResponse] = {
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

private object dataSourcePool {
  private val pool = mutable.Map.empty[String, HikariPool]

  def add(name: String, config: HikariConfig): Unit = {
    pool += (name -> new HikariPool(config))
  }

  def has(name: String): Boolean = {
    pool.contains(name)
  }

  def connection(name: String): Option[Connection] = {
    val storedPool = pool.get(name)
    if (storedPool.isEmpty) {
      return None
    }
    Some(storedPool.get.getConnection)
  }
}
