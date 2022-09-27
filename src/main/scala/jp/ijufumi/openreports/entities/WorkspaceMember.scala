package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime

case class WorkspaceMember(
    workspaceId: String,
    memberId: String,
    createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    versions: Long = 1,
)

class WorkspaceMembers(tag: Tag)
    extends Table[WorkspaceMember](
      tag,
      "workspace_members",
    ) {
  def workspaceId = column[String]("workspace_id", O.PrimaryKey)

  def memberId = column[String]("member_id", O.PrimaryKey)

  def createdAt = column[Timestamp]("created_at")

  def updatedAt = column[Timestamp]("updated_at")

  def versions = column[Long]("versions")

  override def * =
    (
      workspaceId,
      memberId,
      createdAt,
      updatedAt,
      versions,
    ) <> (WorkspaceMember.tupled, WorkspaceMember.unapply)
}
