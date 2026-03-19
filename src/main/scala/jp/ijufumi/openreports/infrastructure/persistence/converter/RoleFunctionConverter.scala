package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{RoleFunction => RoleFunctionModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{RoleFunction => RoleFunctionEntity}

object RoleFunctionConverter {
  def toDomain(entity: RoleFunctionEntity): RoleFunctionModel = {
    RoleFunctionModel(
      entity.id,
      entity.roleId,
      entity.functionId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toEntity(model: RoleFunctionModel): RoleFunctionEntity = {
    RoleFunctionEntity(
      model.id,
      model.roleId,
      model.functionId,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toRoleFunctionEntity(model: RoleFunctionModel): RoleFunctionEntity = toEntity(model)
    implicit def fromRoleFunctionEntity(entity: RoleFunctionEntity): RoleFunctionModel = toDomain(entity)
    implicit def fromRoleFunctionEntity2(entity: Option[RoleFunctionEntity]): Option[RoleFunctionModel] = entity.map(toDomain)
    implicit def fromRoleFunctionEntities(entity: Seq[RoleFunctionEntity]): Seq[RoleFunctionModel] = entity.map(toDomain)
  }
}
