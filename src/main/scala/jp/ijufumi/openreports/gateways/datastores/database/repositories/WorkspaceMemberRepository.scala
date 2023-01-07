package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.entities.WorkspaceMember

trait WorkspaceMemberRepository {
  def getById(workspaceId: String, memberId: String): Option[WorkspaceMember]

  def register(workspaceMember: WorkspaceMember): Option[WorkspaceMember]
}
