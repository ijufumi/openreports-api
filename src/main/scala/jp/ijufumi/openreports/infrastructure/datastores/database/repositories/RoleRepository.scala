package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.Role

trait RoleRepository {
  def getAll: Seq[Role]

  def getByType(permissionType: RoleType): Option[Role]
}
