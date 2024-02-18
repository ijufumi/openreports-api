package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import jp.ijufumi.openreports.utils.Dates
import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import slick.jdbc.PostgresProfile.api._

case class Role(
    id: String,
    roleType: RoleTypes.RoleType,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Roles(tag: Tag)
    extends EntityBase[Role](
      tag,
      "roles",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def roleType = column[RoleTypes.RoleType]("role_type", O.Unique)

  override def * =
    (
      id,
      roleType,
      createdAt,
      updatedAt,
      versions,
    ) <> (Role.tupled, Role.unapply)
}
