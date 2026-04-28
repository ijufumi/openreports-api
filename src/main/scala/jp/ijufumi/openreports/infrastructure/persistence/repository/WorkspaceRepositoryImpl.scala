package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.WorkspaceRepository
import jp.ijufumi.openreports.domain.models.entity.Workspace
import jp.ijufumi.openreports.exceptions.OptimisticLockException
import jp.ijufumi.openreports.infrastructure.persistence.converter.WorkspaceConverter.conversions._
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Workspace => WorkspaceEntity}
import jp.ijufumi.openreports.utils.Dates
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
    val newEntity: WorkspaceEntity =
      workspace.copy(updatedAt = Dates.currentTimestamp(), versions = workspace.versions + 1)
    val q = workspaceQuery.filter(_.id === workspace.id).filter(_.versions === workspace.versions)
    val affected = Await.result(db.run(q.update(newEntity).withPinnedSession), queryTimeout)
    if (affected == 0) {
      throw new OptimisticLockException(
        s"Workspace id=${workspace.id} was modified concurrently or does not exist",
      )
    }
    getById(db, workspace.id)
  }
}
