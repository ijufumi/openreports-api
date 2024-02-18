package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import jp.ijufumi.openreports.presentation.models.requests.UpdateWorkspace
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
    extends EntityBase[Workspace](
      tag,
      "workspaces",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def slug = column[String]("slug")

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
