package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates

case class RoleFunction(
    id: String,
    roleId: String,
    functionId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class RoleFunctions(tag: Tag)
    extends EntityBase[RoleFunction](
      tag,
      "role_functions",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def roleId = column[String]("role_id")
  def functionId = column[String]("function_id")

  override def * =
    (
      id,
      roleId,
      functionId,
      createdAt,
      updatedAt,
      versions,
    ) <> (RoleFunction.tupled, RoleFunction.unapply)
}
