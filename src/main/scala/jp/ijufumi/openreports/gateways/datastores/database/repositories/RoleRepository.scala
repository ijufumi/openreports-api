package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.gateways.datastores.database.entities.Role
import jp.ijufumi.openreports.gateways.datastores.database.entities.enums.RoleTypes._

trait RoleRepository {
  def getAll: Seq[Role]

  def getByType(permissionType: RoleType): Option[Role]
}
