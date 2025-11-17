package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.presentation.responses.{Member, Permission}

trait MemberUseCase {
  def update(memberId: String, name: String, password: String): Option[Member]

  def permissions(memberId: String, workspaceId: String): Option[Permission]
}
