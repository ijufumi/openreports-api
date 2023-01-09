package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.entities.{WorkspaceMember => WorkspaceMemberEntity}

case class WorkspaceMember(workspaceId: String, memberId: String, permissionId: String)

object WorkspaceMember {
  def apply(entity: WorkspaceMemberEntity): WorkspaceMember = {
    WorkspaceMember(entity.workspaceId, entity.memberId, entity.permissionId)
  }
}
