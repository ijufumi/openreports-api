package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.pool.HikariPool
import jp.ijufumi.openreports.repositories.db.DataSourceRepository
import jp.ijufumi.openreports.services.DataSourceService

import java.sql.Connection
import scala.collection.mutable

class DataSourceServiceImpl @Inject() (dataSourceRepository: DataSourceRepository)
    extends DataSourceService {
  def connection(dataSourceId: String): Option[Connection] = {
    val dataSourceOpt = dataSourceRepository.getById(dataSourceId)
    if (dataSourceOpt.isEmpty) {
      return None
    }
    val (dataSource, driverType) = dataSourceOpt.get
    if (!DataSourcePool.has(dataSource.name)) {
      val config = new HikariConfig()
      config.setUsername(dataSource.username)
      config.setPassword(dataSource.password)
      config.setJdbcUrl(dataSource.url)
      config.setAutoCommit(false)
      config.setDriverClassName(driverType.jdbcDriverClass)
      DataSourcePool.add(dataSource.name, config)
      return DataSourcePool.connection(dataSource.name)
    }
    DataSourcePool.connection(dataSource.name)
  }
}

private object DataSourcePool {
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
      return null
    }
    Some(storedPool.get.getConnection)
  }
}
