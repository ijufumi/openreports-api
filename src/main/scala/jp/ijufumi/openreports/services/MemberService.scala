package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.presentation.models.responses.{Member, Permission}

trait MemberService {
  def update(memberId: String, name: String, password: String): Option[Member]

  def permissions(memberId: String, workspaceId: String): Option[Permission]
}
