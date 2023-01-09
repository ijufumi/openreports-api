package jp.ijufumi.openreports.entities.enums

object PermissionTypes extends Enumeration {
  type PermissionType = Value

  val Admin: PermissionType = Value("admin")
  val Developer: PermissionType = Value("developer")
  val Viewer: PermissionType = Value("viewer")
}
