package jp.ijufumi.openreports.presentation.converter

import jp.ijufumi.openreports.domain.models.entity.{Role => RoleModel}
import jp.ijufumi.openreports.presentation.response.{Role => RoleResponse}

object RoleConverter {
  def toResponse(model: RoleModel): RoleResponse = {
    RoleResponse(model.id, model.roleType)
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toRoleResponse(model: RoleModel): RoleResponse = toResponse(model)
    implicit def toRoleResponses(model: Seq[RoleModel]): Seq[RoleResponse] = model.map(toResponse)
  }
}
