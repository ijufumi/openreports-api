package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.pool.HikariPool
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.gateways.datastores.database.repositories.DataSourceRepository
import jp.ijufumi.openreports.services.DataSourceService
import jp.ijufumi.openreports.models.inputs.{CreateDataSource, UpdateDataSource}
import jp.ijufumi.openreports.models.outputs.DataSource

import java.sql.Connection
import scala.collection.mutable

class DataSourceServiceImpl @Inject() (dataSourceRepository: DataSourceRepository)
    extends DataSourceService {
  def connection(workspaceId: String, dataSourceId: String): Connection = {
    val dataSourceOpt = dataSourceRepository.getById(workspaceId, dataSourceId)
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

  override def getDataSources(workspaceId: String): Seq[DataSource] = {
    val dataSources = dataSourceRepository.getAll(workspaceId)
    dataSources.map(d => DataSource(d._1))
  }

  override def getDataSource(workspaceId: String, id: String): Option[DataSource] = {
    val dataSource = dataSourceRepository.getById(workspaceId, id)
    if (dataSource.isEmpty) {
      return None
    }
    Some(DataSource(dataSource.get._1))
  }

  override def registerDataSource(
      workspaceId: String,
      requestVal: CreateDataSource,
  ): Option[DataSource] = {
    None
  }

  override def updateDataSource(
      workspaceId: String,
      id: String,
      updateDataSource: UpdateDataSource,
  ): Option[DataSource] = {
    None
  }

  override def deleteDataSource(workspaceId: String, id: String): Unit = {
    None
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
