package jp.ijufumi.openreports.infrastructure.persistence.database.pool

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.pool.HikariPool
import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import jp.ijufumi.openreports.exceptions.NotFoundException

import java.sql.Connection
import java.util.concurrent.ConcurrentHashMap

object ConnectionPool {
  private val pools = new ConcurrentHashMap[String, HikariPool]()

  def newConnection(
      name: String,
      username: String,
      password: String,
      url: String,
      jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
      maxPoolSize: Integer,
  ): Connection = {
    val pool = pools.computeIfAbsent(
      name,
      _ => buildPool(username, password, url, jdbcDriverClass, maxPoolSize),
    )
    pool.getConnection()
  }

  def newConnection(name: String): Connection = {
    val pool = pools.get(name)
    if (pool == null) {
      throw new NotFoundException
    }
    pool.getConnection()
  }

  private def buildPool(
      username: String,
      password: String,
      url: String,
      jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
      maxPoolSize: Integer,
  ): HikariPool = {
    val config = new HikariConfig()
    config.setUsername(username)
    config.setPassword(password)
    config.setJdbcUrl(url)
    config.setAutoCommit(false)
    config.setMaximumPoolSize(maxPoolSize)
    config.setDriverClassName(jdbcDriverClass.toString)
    new HikariPool(config)
  }
}
