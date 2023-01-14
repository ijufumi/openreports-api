package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.entities.enums.RoleTypes.RoleType
import jp.ijufumi.openreports.entities.{Role => RoleEntity}

case class Role(id: String, roleType: RoleType)

object Role {
  def apply(entity: RoleEntity): Role = {
    Role(entity.id, entity.roleType)
  }
}
