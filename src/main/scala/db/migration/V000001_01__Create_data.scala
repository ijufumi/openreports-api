package db.migration

import jp.ijufumi.openreports.domain.models.value.enums.{
  ActionTypes,
  DBNames,
  JdbcDriverClasses,
  RoleTypes,
}
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import jp.ijufumi.openreports.utils.{Hash, IDs}

import scala.collection.mutable

class V000001_01__Create_data extends BaseJavaMigration {
  override def migrate(context: Context): Unit = {
    val roleIds = role(context)
    var functionIds = mutable.Seq.empty[String]
    ActionTypes.values.foreach { action =>
      {
        val functionId = function(context, "report", action)
        functionIds = functionIds :+ functionId
      }
    }
    roleIds.foreach(roleId => {
      functionIds.foreach(functionId => {
        roleFunction(context, roleId, functionId)
      })
    })
    val workspaceId = workspace(context)
    val memberId = member(context)
    workspaceMember(context, workspaceId, memberId, roleIds(0))
    val driverTypeId = driverType(context)
    val dataSourceId = dataSource(context, driverTypeId, workspaceId)
    val reportGroupId = reportGroup(context, workspaceId)
    for (i <- 1 to 35) {
      val templateId = template(context, workspaceId, s"template-$i")
      val reportId = report(context, templateId, dataSourceId, workspaceId, s"local-$i")
      reportGroupReport(context, reportId, reportGroupId)
    }
  }

  private def role(context: Context): mutable.Seq[String] = {
    var roleIds = mutable.Seq.empty[String]
    RoleTypes.values.foreach { value =>
      {
        val id = IDs.ulid()
        roleIds = roleIds :+ id
        val statement = {
          context.getConnection.prepareStatement(
            s"INSERT INTO roles (id, role_type) VALUES (?, ?)",
          )
        }
        statement.setString(1, id)
        statement.setString(2, value.toString)
        try statement.execute
        finally if (statement != null) statement.close()
      }
    }
    roleIds
  }

  private def function(
      context: Context,
      resource: String,
      action: ActionTypes.ActionType,
  ): String = {
    val id = IDs.ulid()
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO functions (id, resource, action) VALUES (?, ?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, resource)
    statement.setString(3, action.toString)
    try statement.execute
    finally if (statement != null) statement.close()
    id
  }

  private def roleFunction(context: Context, roleId: String, functionId: String): Unit = {
    val id = IDs.ulid()
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO role_functions (id, role_id, function_id) VALUES (?, ?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, roleId)
    statement.setString(3, functionId)
    try statement.execute
    finally if (statement != null) statement.close()
  }

  private def member(context: Context): String = {
    val id = IDs.ulid()
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

  private def workspace(context: Context): String = {
    val id = IDs.ulid()
    val name = "Root Workspace"
    val slug = "root"
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO workspaces (id, name, slug) VALUES (?, ?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, name)
    statement.setString(3, slug)
    try statement.execute
    finally if (statement != null) statement.close()
    id
  }

  private def workspaceMember(
      context: Context,
      workspaceId: String,
      memberId: String,
      roleId: String,
  ): Unit = {
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO workspace_members (workspace_id, member_id, role_id) VALUES (?, ?, ?)",
      )
    }
    statement.setString(1, workspaceId)
    statement.setString(2, memberId)
    statement.setString(3, roleId)
    try statement.execute
    finally if (statement != null) statement.close()

  }

  private def driverType(context: Context): String = {
    var driverTypeId = ""
    JdbcDriverClasses.values.foreach(value => {
      val id = IDs.ulid()
      if (driverTypeId.isEmpty) {
        driverTypeId = id
      }
      val name = DBNames.getDbNameByDriverClass(value)
      val driverClass = value.toString
      val statement = {
        context.getConnection.prepareStatement(
          s"INSERT INTO driver_types (id, name, jdbc_driver_class) VALUES (?, ?, ?)",
        )
      }
      statement.setString(1, id)
      statement.setString(2, name.toString)
      statement.setString(3, driverClass)
      try statement.execute
      finally if (statement != null) statement.close()
    })

    driverTypeId
  }

  private def dataSource(context: Context, driverTypeId: String, workspaceId: String): String = {
    val dbHost = sys.env.getOrElse("DB_HOST", "localhost")
    val dbName = sys.env.getOrElse("DB_NAME", "openreports")
    val dbUser = sys.env.getOrElse("DB_USER", "postgres")
    val dbPassword = sys.env.getOrElse("DB_PASSWORD", "password")
    val dbPort = sys.env.getOrElse("DB_PORT", "5432")
    val id = IDs.ulid()
    val name = "local"

    val url = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName)
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO data_sources (id, name, url, username, password, driver_type_id, workspace_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, name)
    statement.setString(3, url)
    statement.setString(4, dbUser)
    statement.setString(5, dbPassword)
    statement.setString(6, driverTypeId)
    statement.setString(7, workspaceId)
    try statement.execute
    finally if (statement != null) statement.close()

    id
  }

  private def template(context: Context, workspaceId: String, name: String): String = {
    val id = IDs.ulid()
    val filePath = "sample.xlsx"
    val storageType = "localSeed"
    val fileSize = 1
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO report_templates (id, name, file_path, workspace_id, storage_type, file_size) VALUES (?, ?, ?, ?, ?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, name)
    statement.setString(3, filePath)
    statement.setString(4, workspaceId)
    statement.setString(5, storageType)
    statement.setLong(6, fileSize)
    try statement.execute
    finally if (statement != null) statement.close()

    id
  }

  private def reportGroup(
      context: Context,
      workspaceId: String,
  ): String = {
    val id = IDs.ulid()
    val name = "sample group"
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO report_groups (id, name, workspace_id) VALUES (?, ?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, name)
    statement.setString(3, workspaceId)
    try statement.execute
    finally if (statement != null) statement.close()

    id
  }

  private def report(
      context: Context,
      templateId: String,
      dataSourceId: String,
      workspaceId: String,
      name: String,
  ): String = {
    val id = IDs.ulid()
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO reports (id, name, report_template_id, data_source_id, workspace_id) VALUES (?, ?, ?, ?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, name)
    statement.setString(3, templateId)
    statement.setString(4, dataSourceId)
    statement.setString(5, workspaceId)
    try statement.execute
    finally if (statement != null) statement.close()
    id
  }

  private def reportGroupReport(
      context: Context,
      reportId: String,
      reportGroupId: String,
  ): Unit = {
    val id = IDs.ulid()
    val statement = {
      context.getConnection.prepareStatement(
        s"INSERT INTO report_group_reports (id, report_id, report_group_id) VALUES (?, ?, ?)",
      )
    }
    statement.setString(1, id)
    statement.setString(2, reportId)
    statement.setString(3, reportGroupId)
    try statement.execute
    finally if (statement != null) statement.close()
  }
}
