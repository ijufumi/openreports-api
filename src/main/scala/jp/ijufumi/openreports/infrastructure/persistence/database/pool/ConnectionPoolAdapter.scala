package jp.ijufumi.openreports.infrastructure.persistence.database.pool

import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import jp.ijufumi.openreports.domain.port.ConnectionPoolPort

import java.sql.Connection

class ConnectionPoolAdapter extends ConnectionPoolPort {
  override def newConnection(
      name: String,
      username: String,
      password: String,
      url: String,
      jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
      maxPoolSize: Integer,
  ): Connection = {
    ConnectionPool.newConnection(name, username, password, url, jdbcDriverClass, maxPoolSize)
  }
}
