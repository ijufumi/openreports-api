package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.WorkspaceMember
import jp.ijufumi.openreports.entities.queries.{workspaceMemberQuery => query}
import jp.ijufumi.openreports.repositories.db.WorkspaceMemberRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class WorkspaceMemberRepositoryImpl @Inject() (db: Database) extends WorkspaceMemberRepository {
  override def getById(workspaceId: String, memberId: String): Option[WorkspaceMember] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.memberId === memberId)
    val workspaceMembers = Await.result(db.run(getById.result), queryTimeout)
    if (workspaceMembers.isEmpty) {
      return None
    }
    Some(workspaceMembers.head)

  }

  override def register(workspaceMember: WorkspaceMember): Option[WorkspaceMember] = {
    val register = (query += workspaceMember).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(workspaceMember.workspaceId, workspaceMember.memberId)
  }
}
