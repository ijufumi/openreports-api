package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.presentation.response.Role

trait RoleUseCase {
  def getRoles: Seq[Role]
}
