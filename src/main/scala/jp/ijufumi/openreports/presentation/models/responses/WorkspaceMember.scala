package jp.ijufumi.openreports.presentation.models.responses

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  Member => MemberEntity,
  WorkspaceMember => WorkspaceMemberEntity,
}
import jp.ijufumi.openreports.presentation.models.requests.UpdateWorkspaceMember
import jp.ijufumi.openreports.utils.Dates

case class WorkspaceMember(
    workspaceId: String,
    memberId: String,
    roleId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    member: Option[Member] = None,
) {
  def toEntity: WorkspaceMemberEntity = {
    WorkspaceMemberEntity(
      this.workspaceId,
      this.memberId,
      this.roleId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def copyForUpdate(input: UpdateWorkspaceMember): WorkspaceMember = {
    this.copy(roleId = input.roleId)
  }
}

object WorkspaceMember {
  def apply(entity: (WorkspaceMemberEntity, MemberEntity)): WorkspaceMember = {
    WorkspaceMember(
      entity._1.workspaceId,
      entity._1.memberId,
      entity._1.roleId,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      Some(Member(entity._2)),
    )
  }
  def apply(entity: WorkspaceMemberEntity): WorkspaceMember = {
    WorkspaceMember(
      entity.workspaceId,
      entity.memberId,
      entity.roleId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
}
