package jp.ijufumi.openreports.service.support

import scalikejdbc.{Commons2ConnectionPoolFactory, ConnectionPool}

object ConnectionFactory {
  def getConnection: java.sql.Connection = {
    getConnection(
      "jdbc:postgresql://localhost:5432/openreports",
      "postgres",
      "password"
    )
  }

  def getConnection(
    url: String,
    user: String,
    password: String
  ): java.sql.Connection = {
    getConnectionPool(url, user, password).borrow()
  }

  def getConnectionPool(
    url: String,
    user: String,
    password: String
  ): ConnectionPool = {
    Commons2ConnectionPoolFactory(url, user, password)
  }
}
