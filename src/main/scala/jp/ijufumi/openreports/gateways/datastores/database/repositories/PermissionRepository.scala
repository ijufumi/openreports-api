package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.entities.Permission
import jp.ijufumi.openreports.entities.enums.PermissionTypes._

trait PermissionRepository {
  def getAll: Seq[Permission]

  def getByType(permissionType: PermissionType): Option[Permission]
}
