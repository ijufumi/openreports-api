package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import jp.ijufumi.openreports.utils.Hash

class V000001_01__Create_user extends BaseJavaMigration {
  override def migrate(context: Context): Unit = {
    try {
      val email = "root@9jufumi.jp"
      val password = Hash.hmacSha256("password")
      val name = "Root User"
      val statement: java.sql.PreparedStatement = {
        context.getConnection.prepareStatement(
          s"INSERT INTO members (email, password, name) VALUES (?, ?, ?)",
        )
      }
      statement.setString(1, email)
      statement.setString(2, password)
      statement.setString(3, name)
      try statement.execute
      finally if (statement != null) statement.close()
    }
  }
}
