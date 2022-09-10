package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.{Workspace, Workspaces}
import jp.ijufumi.openreports.repositories.db.WorkspaceRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class WorkspaceRepositoryImpl @Inject() (db: Database) extends WorkspaceRepository {
  private lazy val query = TableQuery[Workspaces]

  override def getById(id: String): Option[Workspace] = {
    val getById = query
      .filter(_.id === id)
    val workspaces = Await.result(db.run(getById.result), Duration("10s"))
    if (workspaces.isEmpty) {
      return None
    }
    Option(workspaces.head)

  }

  override def register(workspace: Workspace): Option[Workspace] = {
    val register = (query += workspace).withPinnedSession
    Await.result(db.run(register), Duration("1m"))
    getById(workspace.id)
  }
}
