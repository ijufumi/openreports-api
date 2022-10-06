package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class Workspace(
    id: String,
    name: String,
    slug: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Workspaces(tag: Tag)
    extends Table[Workspace](
      tag,
      "workspaces",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def slug = column[String]("slug")
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      name,
      slug,
      createdAt,
      updatedAt,
      versions,
    ) <> (Workspace.tupled, Workspace.unapply)
}
