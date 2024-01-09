package jp.ijufumi.openreports.gateways.datastores.database.entities

import jp.ijufumi.openreports.models.inputs.UpdateWorkspace
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class Workspace(
    id: String,
    name: String,
    slug: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def copyForUpdate(input: UpdateWorkspace): Workspace = {
    this.copy(name = input.name)
  }
}

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
