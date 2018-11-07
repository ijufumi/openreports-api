package jp.ijufumi.openreports.service.support

import scalikejdbc.ConnectionPool

object ConnectionFactory {
  def getConnection: java.sql.Connection = {
    getConnection(
      ConnectionPool.DEFAULT_NAME,
      "jdbc:postgresql://localhost:5432/openreports",
      "postgres",
      "password"
    )
  }

  def getConnection(
    name: Symbol,
    url: String,
    user: String,
    password: String
  ): java.sql.Connection = {
    if (!ConnectionPool.isInitialized(name)) {
      ConnectionPool.add(name, url, user, password)
    }
    ConnectionPool.get().borrow()
  }
}
