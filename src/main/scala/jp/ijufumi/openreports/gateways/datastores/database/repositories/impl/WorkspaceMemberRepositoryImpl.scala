package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.{Member, WorkspaceMember}
import queries.{memberQuery, workspaceMemberQuery => query}
import jp.ijufumi.openreports.gateways.datastores.database.repositories.WorkspaceMemberRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

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

  override def gets(workspaceId: String): Seq[WorkspaceMember] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
    Await.result(db.run(getById.result), queryTimeout)
  }

  override def getWithMemberById(workspaceId: String, memberId: String): Option[(WorkspaceMember, Member)] = {
    val getById = query
      .join(memberQuery)
      .on(_.memberId === _.id)
      .filter(_._1.workspaceId === workspaceId)
      .filter(_._1.memberId === memberId)
    val workspaceMembers = Await.result(db.run(getById.result), queryTimeout)
    if (workspaceMembers.isEmpty) {
      return None
    }
    Some(workspaceMembers.head)
  }

  override def getsWithMember(workspaceId: String): Seq[(WorkspaceMember, Member)] = {
    val getById = query
      .join(memberQuery)
      .on(_.memberId === _.id)
      .filter(_._1.workspaceId === workspaceId)
    Await.result(db.run(getById.result), queryTimeout)
  }

  override def register(workspaceMember: WorkspaceMember): Option[WorkspaceMember] = {
    val register = (query += workspaceMember).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(workspaceMember.workspaceId, workspaceMember.memberId)
  }
}
