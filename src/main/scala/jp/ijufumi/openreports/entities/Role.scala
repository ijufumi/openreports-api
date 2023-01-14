package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.entities.enums.RoleTypes.RoleType
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class Role(
    id: String,
    roleType: RoleType,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Roles(tag: Tag)
    extends Table[Role](
      tag,
      "roles",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def roleType = column[RoleType]("role_type", O.Unique)
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      roleType,
      createdAt,
      updatedAt,
      versions,
    ) <> (Role.tupled, Role.unapply)
}
