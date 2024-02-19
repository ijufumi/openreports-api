package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.presentation.models.responses.WorkspaceMember

trait WorkspaceMemberRepository {
  def getById(workspaceId: String, memberId: String): Option[WorkspaceMember]

  def gets(workspaceId: String): Seq[WorkspaceMember]

  def getsByMemberId(memberId: String): Seq[WorkspaceMember]

  def getByIdWithMember(workspaceId: String, memberId: String): Option[WorkspaceMember]

  def getsWithMember(workspaceId: String): Seq[WorkspaceMember]

  def register(workspaceMember: WorkspaceMember): Option[WorkspaceMember]

  def update(workspaceMember: WorkspaceMember): Unit

  def delete(workspaceId: String, memberId: String): Unit
}
