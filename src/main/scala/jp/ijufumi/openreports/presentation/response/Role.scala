package jp.ijufumi.openreports.presentation.response

import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Role => RoleEntity}

case class Role(id: String, roleType: RoleTypes.RoleType)

object Role {
  def apply(entity: RoleEntity): Role = {
    Role(entity.id, entity.roleType)
  }
}
