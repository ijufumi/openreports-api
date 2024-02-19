package jp.ijufumi.openreports.presentation.models.responses

import jp.ijufumi.openreports.utils.Dates
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  RoleFunction => RoleFunctionEntity,
}

import scala.language.implicitConversions

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
  implicit def toEntity(model: RoleFunction): RoleFunctionEntity = {
    model.toEntity
  }

  implicit def toModel(entity: RoleFunctionEntity): RoleFunction = {
    RoleFunction(
      entity.id,
      entity.roleId,
      entity.functionId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

}
