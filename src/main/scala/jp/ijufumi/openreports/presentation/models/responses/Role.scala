package jp.ijufumi.openreports.presentation.models.responses

import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{Role => RoleEntity}

case class Role(id: String, roleType: RoleTypes.RoleType)

object Role {
  def apply(entity: RoleEntity): Role = {
    Role(entity.id, entity.roleType)
  }
}
