package jp.ijufumi.openreports.domain.models.value.enums

object RoleTypes extends Enumeration {
  type RoleType = Value

  val Admin: RoleType = Value("admin")
  val Developer: RoleType = Value("developer")
  val Viewer: RoleType = Value("viewer")
}
