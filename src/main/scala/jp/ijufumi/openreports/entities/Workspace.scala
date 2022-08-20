package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime

case class Workspace(
    id: String,
    name: String,
    createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    versions: Long = 1,
)

class Workspaces(tag: Tag)
    extends Table[Workspace](
      tag,
      "workspaces",
    ) {
  def id = column[String]("id", O.PrimaryKey)

  def name = column[String]("name")

  def createdAt = column[Timestamp]("created_at")

  def updatedAt = column[Timestamp]("updated_at")

  def versions = column[Long]("versions")

  override def * =
    (
      id,
      name,
      createdAt,
      updatedAt,
      versions,
    ) <> (Workspace.tupled, Workspace.unapply)
}
