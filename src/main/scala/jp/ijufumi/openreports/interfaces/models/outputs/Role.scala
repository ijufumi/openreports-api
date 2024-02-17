package jp.ijufumi.openreports.interfaces.models.outputs

import jp.ijufumi.openreports.interfaces.models.value.enums.RoleTypes.RoleType
import jp.ijufumi.openreports.gateways.datastores.database.entities.{Role => RoleEntity}

case class Role(id: String, roleType: RoleType)

object Role {
  def apply(entity: RoleEntity): Role = {
    Role(entity.id, entity.roleType)
  }
}
