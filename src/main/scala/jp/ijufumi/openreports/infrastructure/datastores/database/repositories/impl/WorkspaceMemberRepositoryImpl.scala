package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import com.google.inject.Inject
import queries.{memberQuery, workspaceMemberQuery => query}
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.WorkspaceMemberRepository
import jp.ijufumi.openreports.domain.models.entity.WorkspaceMember
import jp.ijufumi.openreports.domain.models.entity.WorkspaceMember.conversions._
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
    val result = Await.result(db.run(getById.result), queryTimeout)
    result
  }

  override def getsByMemberId(memberId: String): Seq[WorkspaceMember] = {
    val getById = query
      .filter(_.memberId === memberId)
    val result = Await.result(db.run(getById.result), queryTimeout)
    result
  }

  override def getByIdWithMember(
      workspaceId: String,
      memberId: String,
  ): Option[WorkspaceMember] = {
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

  override def getsWithMember(workspaceId: String): Seq[WorkspaceMember] = {
    val getById = query
      .join(memberQuery)
      .on(_.memberId === _.id)
      .filter(_._1.workspaceId === workspaceId)
    val result = Await.result(db.run(getById.result), queryTimeout)
    result
  }

  override def register(workspaceMember: WorkspaceMember): Option[WorkspaceMember] = {
    val register = (query += workspaceMember).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(workspaceMember.workspaceId, workspaceMember.memberId)
  }

  override def update(workspaceMember: WorkspaceMember): Unit = {
    query.insertOrUpdate(workspaceMember).withPinnedSession
  }

  override def delete(workspaceId: String, memberId: String): Unit = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
