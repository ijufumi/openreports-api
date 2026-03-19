package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.domain.models.entity.{Role => RoleModel}

trait RoleUseCase {
  def getRoles: Seq[RoleModel]
}
