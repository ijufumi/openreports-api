package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.models.inputs.UpdateWorkspaceMember
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
  def workspaceId = column[String]("workspace_id", O.PrimaryKey)
  def memberId = column[String]("member_id", O.PrimaryKey)
  def roleId = column[String]("role_id")

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
