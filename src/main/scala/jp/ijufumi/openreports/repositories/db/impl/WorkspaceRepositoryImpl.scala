package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.Workspace
import jp.ijufumi.openreports.entities.queries.{workspaceQuery => query, workspaceMemberQuery}
import jp.ijufumi.openreports.repositories.db.WorkspaceRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class WorkspaceRepositoryImpl @Inject() (db: Database) extends WorkspaceRepository {
  override def getById(id: String): Option[Workspace] = {
    val getById = query
      .filter(_.id === id)
    val workspaces = Await.result(db.run(getById.result), Duration("10s"))
    if (workspaces.isEmpty) {
      return None
    }
    Some(workspaces.head)
  }

  override def getsByMemberId(memberId: String): Seq[Workspace] = {
    val getsByMemberId = query.join(workspaceMemberQuery).on(_.id === _.workspaceId).filter(_._2.memberId === memberId)
    val workspaces = Await.result(db.run(getsByMemberId.result), Duration("10s"))
    workspaces.map(m => m._1)
  }

  override def register(workspace: Workspace): Option[Workspace] = {
    val register = (query += workspace).withPinnedSession
    Await.result(db.run(register), Duration("1m"))
    getById(workspace.id)
  }

  override def update(workspace: Workspace): Unit = {
    query.insertOrUpdate(workspace).withPinnedSession
  }
}
