package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.pool.HikariPool
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.repositories.db.DataSourceRepository
import jp.ijufumi.openreports.services.DataSourceService

import scala.collection.mutable.Map
import java.sql.Connection
import scala.collection.mutable

class DataSourceServiceImpl @Inject() (dataSourceRepository: DataSourceRepository)
    extends DataSourceService {
  def connection(dataSourceId: String): Connection = {
    val dataSourceOpt = dataSourceRepository.getById(dataSourceId)
    if (dataSourceOpt.isEmpty) {
      throw new NotFoundException()
    }
    val (dataSource, driverType) = dataSourceOpt.get
    val connection = DataSourcePool.connection(dataSource.name)
    if (connection.isEmpty) {
      val config = new HikariConfig()
      config.setUsername(dataSource.username)
      config.setPassword(dataSource.password)
      config.setJdbcUrl(dataSource.url)
      config.setAutoCommit(false)
      config.setDriverClassName(driverType.jdbcDriverClass)
      DataSourcePool.addPool(dataSource.name, config)
      return DataSourcePool.connection(dataSource.name).get
    }
    connection.get
  }
}

private object DataSourcePool {
  private val pool = mutable.Map.empty[String, HikariPool]

  def addPool(name: String, config: HikariConfig): Unit = {
    pool += (name -> new HikariPool(config))
  }

  def connection(name: String): Option[Connection] = {
    val storedPool = pool.get(name)
    if (storedPool.isEmpty) {
      return null
    }
    Some(storedPool.get.getConnection)
  }
}
