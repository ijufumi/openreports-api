package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import queries.{memberQuery, workspaceMemberQuery => query}
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.WorkspaceMemberRepository
import jp.ijufumi.openreports.domain.models.entity.WorkspaceMember
import jp.ijufumi.openreports.domain.models.entity.WorkspaceMember.conversions._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class WorkspaceMemberRepositoryImpl extends WorkspaceMemberRepository {
  override def getById(
      db: Database,
      workspaceId: String,
      memberId: String,
  ): Option[WorkspaceMember] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.memberId === memberId)
    val workspaceMembers = Await.result(db.run(getById.result), queryTimeout)
    if (workspaceMembers.isEmpty) {
      return None
    }
    Some(workspaceMembers.head)
  }

  override def gets(db: Database, workspaceId: String): Seq[WorkspaceMember] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
    val result = Await.result(db.run(getById.result), queryTimeout)
    result
  }

  override def getsByMemberId(db: Database, memberId: String): Seq[WorkspaceMember] = {
    val getById = query
      .filter(_.memberId === memberId)
    val result = Await.result(db.run(getById.result), queryTimeout)
    result
  }

  override def getByIdWithMember(
      db: Database,
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

  override def getsWithMember(db: Database, workspaceId: String): Seq[WorkspaceMember] = {
    val getById = query
      .join(memberQuery)
      .on(_.memberId === _.id)
      .filter(_._1.workspaceId === workspaceId)
    val result = Await.result(db.run(getById.result), queryTimeout)
    result
  }

  override def register(db: Database, workspaceMember: WorkspaceMember): Option[WorkspaceMember] = {
    val register = (query += workspaceMember).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, workspaceMember.workspaceId, workspaceMember.memberId)
  }

  override def update(db: Database, workspaceMember: WorkspaceMember): Unit = {
    query.insertOrUpdate(workspaceMember).withPinnedSession
  }

  override def delete(db: Database, workspaceId: String, memberId: String): Unit = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
