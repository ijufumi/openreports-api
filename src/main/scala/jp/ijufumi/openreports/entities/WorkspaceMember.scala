package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class WorkspaceMember(
    workspaceId: String,
    memberId: String,
    permissionId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class WorkspaceMembers(tag: Tag)
    extends Table[WorkspaceMember](
      tag,
      "workspace_members",
    ) {
  def workspaceId = column[String]("workspace_id", O.PrimaryKey)
  def memberId = column[String]("member_id", O.PrimaryKey)
  def permissionId = column[String]("permission_id")
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      workspaceId,
      memberId,
      permissionId,
      createdAt,
      updatedAt,
      versions,
    ) <> (WorkspaceMember.tupled, WorkspaceMember.unapply)
}
