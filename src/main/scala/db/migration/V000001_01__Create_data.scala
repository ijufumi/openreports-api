package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import jp.ijufumi.openreports.utils.{Hash, ID}

class V000001_01__Create_data extends BaseJavaMigration {
  override def migrate(context: Context): Unit = {
    val workspaceId = workspace(context)
    val memberId = member(context)
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO workspace_members (workspaceId, memberId) VALUES (?, ?)",
      )
    }
    statement.setString(1, workspaceId)
    statement.setString(2, memberId)
    try statement.execute
    finally if (statement != null) statement.close()
  }

  def member(context: Context): String = {
    val id = ID.ulid()
    val email = "root@ijufumi.jp"
    val password = Hash.hmacSha256("password")
    val name = "Root User"
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO members (id, email, password, name) VALUES (?, ?, ?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, email)
    statement.setString(3, password)
    statement.setString(4, name)
    try statement.execute
    finally if (statement != null) statement.close()
    id
  }

  def workspace(context: Context): String = {
    val id = ID.ulid()
    val name = "Root Workspace"
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO workspaces (id, name) VALUES (?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, name)
    try statement.execute
    finally if (statement != null) statement.close()
    id
  }

}
