package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  RoleFunction => RoleFunctionEntity,
}
import jp.ijufumi.openreports.utils.Dates

case class RoleFunction(
    id: String,
    roleId: String,
    functionId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: RoleFunctionEntity = {
    RoleFunctionEntity(
      this.id,
      this.roleId,
      this.functionId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
}

object RoleFunction {
  def apply(entity: RoleFunctionEntity): RoleFunction = {
    RoleFunction(
      entity.id,
      entity.roleId,
      entity.functionId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toRoleFunctionEntity(model: RoleFunction): RoleFunctionEntity = {
      model.toEntity
    }

    implicit def fromRoleFunctionEntity(entity: RoleFunctionEntity): RoleFunction = {
      RoleFunction(entity)
    }

    implicit def fromRoleFunctionEntity2(
        entity: Option[RoleFunctionEntity],
    ): Option[RoleFunction] = {
      entity.map(e => RoleFunction(e))
    }

    implicit def fromRoleFunctionEntities(
        entity: Seq[RoleFunctionEntity],
    ): Seq[RoleFunction] = {
      entity.map(e => RoleFunction(e))
    }
  }
}
