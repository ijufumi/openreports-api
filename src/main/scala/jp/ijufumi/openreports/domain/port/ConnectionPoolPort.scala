package jp.ijufumi.openreports.domain.port

import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses

import java.sql.Connection

trait ConnectionPoolPort {
  def newConnection(
      name: String,
      username: String,
      password: String,
      url: String,
      jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
      maxPoolSize: Integer,
  ): Connection
}
