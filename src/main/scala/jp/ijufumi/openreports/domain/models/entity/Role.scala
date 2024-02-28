package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{Role => RoleEntity}
import jp.ijufumi.openreports.presentation.models.responses.{Role => RoleResponse}
import jp.ijufumi.openreports.utils.Dates

case class Role(
    id: String,
    roleType: RoleTypes.RoleType,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toResponse: RoleResponse = {
    RoleResponse(
      this.id,
      this.roleType,
    )
  }
}

object Role {
  def apply(entity: RoleEntity): Role = {
    Role(entity.id, entity.roleType, entity.createdAt, entity.updatedAt, entity.versions)
  }
}
