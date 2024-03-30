package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.WorkspaceMember
import slick.jdbc.JdbcBackend.Database

trait WorkspaceMemberRepository {
  def getById(db: Database, workspaceId: String, memberId: String): Option[WorkspaceMember]

  def gets(db: Database, workspaceId: String): Seq[WorkspaceMember]

  def getsByMemberId(db: Database, memberId: String): Seq[WorkspaceMember]

  def getByIdWithMember(db: Database, workspaceId: String, memberId: String): Option[WorkspaceMember]

  def getsWithMember(db: Database, workspaceId: String): Seq[WorkspaceMember]

  def register(db: Database, workspaceMember: WorkspaceMember): Option[WorkspaceMember]

  def update(db: Database, workspaceMember: WorkspaceMember): Unit

  def delete(db: Database, workspaceId: String, memberId: String): Unit
}
