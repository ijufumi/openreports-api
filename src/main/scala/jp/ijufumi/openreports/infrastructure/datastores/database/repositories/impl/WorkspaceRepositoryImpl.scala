package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.WorkspaceRepository
import jp.ijufumi.openreports.domain.models.entity.Workspace
import jp.ijufumi.openreports.domain.models.entity.Workspace.conversions._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class WorkspaceRepositoryImpl extends WorkspaceRepository {
  override def getById(db: Database, id: String): Option[Workspace] = {
    val getById = workspaceQuery
      .filter(_.id === id)
    val workspaces = Await.result(db.run(getById.result), queryTimeout)
    if (workspaces.isEmpty) {
      return None
    }
    Some(workspaces.head)
  }

  override def getsByMemberId(db: Database, memberId: String): Seq[Workspace] = {
    val getsByMemberId =
      workspaceQuery
        .join(workspaceMemberQuery)
        .on(_.id === _.workspaceId)
        .filter(_._2.memberId === memberId)
    val workspaces = Await.result(db.run(getsByMemberId.result), queryTimeout)
    workspaces
  }

  override def register(db: Database, workspace: Workspace): Option[Workspace] = {
    val register = (workspaceQuery += workspace).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, workspace.id)
  }

  override def update(db: Database, workspace: Workspace): Option[Workspace] = {
    workspaceQuery.insertOrUpdate(workspace).withPinnedSession
    getById(db, workspace.id)
  }
}
