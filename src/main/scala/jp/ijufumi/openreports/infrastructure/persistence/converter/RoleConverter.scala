package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{Role => RoleModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Role => RoleEntity}

object RoleConverter {
  def toDomain(entity: RoleEntity): RoleModel = {
    RoleModel(entity.id, entity.roleType, entity.createdAt, entity.updatedAt, entity.versions)
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def fromRoleEntity(entity: RoleEntity): RoleModel = toDomain(entity)
    implicit def fromRoleEntity2(entity: Option[RoleEntity]): Option[RoleModel] = entity.map(toDomain)
    implicit def fromRoleEntities(entity: Seq[RoleEntity]): Seq[RoleModel] = entity.map(toDomain)
  }
}
