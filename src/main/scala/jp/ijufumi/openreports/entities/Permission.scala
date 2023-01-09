package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class Permission(
    id: String,
    name: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Permissions(tag: Tag)
    extends Table[Permission](
      tag,
      "permissions",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      name,
      createdAt,
      updatedAt,
      versions,
    ) <> (Permission.tupled, Permission.unapply)
}
