package jp.ijufumi.openreports.gateways.datastores.database.entities.enums

import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._

object RoleTypes extends Enumeration {
  type RoleType = Value

  val Admin: RoleType = Value("admin")
  val Developer: RoleType = Value("developer")
  val Viewer: RoleType = Value("viewer")

  implicit val storageTypeMapper: JdbcType[RoleType] with BaseTypedType[RoleType] =
    MappedColumnType.base[RoleTypes.RoleType, String](
      e => e.toString,
      s => RoleTypes.withName(s),
    )
}
