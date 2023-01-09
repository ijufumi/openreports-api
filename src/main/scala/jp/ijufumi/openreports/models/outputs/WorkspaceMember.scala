package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.entities.{WorkspaceMember => WorkspaceMemberEntity, Member => MemberEntity}

case class WorkspaceMember(workspaceId: String, memberId: String, email: String, permissionId: String)

object WorkspaceMember {
  def apply(entity: WorkspaceMemberEntity, member: MemberEntity): WorkspaceMember = {
    WorkspaceMember(entity.workspaceId, entity.memberId, member.email, entity.permissionId)
  }
}
