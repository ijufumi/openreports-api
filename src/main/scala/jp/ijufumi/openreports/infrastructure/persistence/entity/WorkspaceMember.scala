package jp.ijufumi.openreports.infrastructure.persistence.entity

import jp.ijufumi.openreports.presentation.request.UpdateWorkspaceMember
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class WorkspaceMember(
    workspaceId: String,
    memberId: String,
    roleId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def copyForUpdate(input: UpdateWorkspaceMember): WorkspaceMember = {
    this.copy(roleId = input.roleId)
  }
}

class WorkspaceMembers(tag: Tag)
    extends EntityBase[WorkspaceMember](
      tag,
      "workspace_members",
    ) {
  def workspaceId = column[String]("workspace_id")
  def memberId = column[String]("member_id")
  def roleId = column[String]("role_id")

  def pk = primaryKey("pk_workspace_members", ("workspace_id", "member_id"))

  override def * =
    (
      workspaceId,
      memberId,
      roleId,
      createdAt,
      updatedAt,
      versions,
    ) <> (WorkspaceMember.tupled, WorkspaceMember.unapply)
}
