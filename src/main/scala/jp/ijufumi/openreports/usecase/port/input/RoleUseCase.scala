package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.presentation.responses.Role

trait RoleUseCase {
  def getRoles: Seq[Role]
}
