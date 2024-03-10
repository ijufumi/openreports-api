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

  object conversions {
    import scala.language.implicitConversions

    implicit def fromRoleEntity(entity: RoleEntity): Role = {
      Role(entity)
    }

    implicit def fromRoleEntity2(entity: Option[RoleEntity]): Option[Role] = {
      entity.map(e => Role(e))
    }

    implicit def fromRoleEntities(entity: Seq[RoleEntity]): Seq[Role] = {
      entity.map(e => Role(e))
    }

    implicit def toRoleResponse(model: Role): RoleResponse = {
      model.toResponse
    }

    implicit def toRoleResponse2(model: Option[Role]): Option[RoleResponse] = {
      model.map(m => m.toResponse)
    }

    implicit def toRoleResponses(model: Seq[Role]): Seq[RoleResponse] = {
      model.map(m => m.toResponse)
    }
  }
}
