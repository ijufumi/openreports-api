package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Role
import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes

trait RoleRepository {
  def getAll: Seq[Role]

  def getByType(permissionType: RoleTypes.RoleType): Option[Role]
}
