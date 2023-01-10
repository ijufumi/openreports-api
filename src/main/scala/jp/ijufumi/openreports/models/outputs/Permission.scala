package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.entities.{Permission => PermissionEntity}

case class Permission(id: String, name: String)

object Permission {
  def apply(entity: PermissionEntity): Permission = {
    Permission(entity.id, entity.name)
  }
}
