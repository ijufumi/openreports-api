package jp.ijufumi.openreports.service.support

import scalikejdbc.Commons2ConnectionPoolFactory

object ConnectionFactory {
  def getConnection(url: String,
                    user: String,
                    password: String): java.sql.Connection = {
    Commons2ConnectionPoolFactory(url, user, password).borrow()
  }
}
