package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.entities.{Member, WorkspaceMember}

trait WorkspaceMemberRepository {
  def getById(workspaceId: String, memberId: String): Option[WorkspaceMember]

  def gets(workspaceId: String): Seq[WorkspaceMember]

  def getWithMemberById(workspaceId: String, memberId: String): Option[(WorkspaceMember, Member)]

  def getsWithMember(workspaceId: String): Seq[(WorkspaceMember, Member)]

  def register(workspaceMember: WorkspaceMember): Option[WorkspaceMember]
}
