package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.models.value.enums.RoleTypes.RoleType
import jp.ijufumi.openreports.gateways.datastores.database.entities.{Role => RoleEntity}

case class Role(id: String, roleType: RoleType)

object Role {
  def apply(entity: RoleEntity): Role = {
    Role(entity.id, entity.roleType)
  }
}
