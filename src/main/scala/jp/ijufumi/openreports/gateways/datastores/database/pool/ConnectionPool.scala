package jp.ijufumi.openreports.gateways.datastores.database.pool

import jp.ijufumi.openreports.exceptions.NotFoundException
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.pool.HikariPool
import jp.ijufumi.openreports.models.value.enums.JdbcDriverClasses.JdbcDriverClass

import java.sql.Connection
import scala.collection.mutable

object ConnectionPool {
  private val pool = mutable.Map.empty[String, HikariPool]

  def newConnection(
      name: String,
      username: String,
      password: String,
      url: String,
      jdbcDriverClass: JdbcDriverClass,
  ): Connection = {
    if (has(name)) {
      return pool(name).getConnection
    }

    val config = new HikariConfig()
    config.setUsername(username)
    config.setPassword(password)
    config.setJdbcUrl(url)
    config.setAutoCommit(false)
    config.setDriverClassName(jdbcDriverClass.toString)
    pool += (name -> new HikariPool(config))

    newConnection(name)
  }

  def newConnection(name: String): Connection = {
    if (!has(name)) {
      throw new NotFoundException
    }

    pool(name).getConnection()
  }

  def has(name: String): Boolean = {
    pool.contains(name)
  }
}
