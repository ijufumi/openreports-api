package jp.ijufumi.openreports.service.support

import scalikejdbc.ConnectionPool

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
    if (!ConnectionPool.isInitialized(ConnectionPool.DEFAULT_NAME)) {
      ConnectionPool.add(ConnectionPool.DEFAULT_NAME, url, user, password)
    }
    ConnectionPool.get().borrow()
  }
}
