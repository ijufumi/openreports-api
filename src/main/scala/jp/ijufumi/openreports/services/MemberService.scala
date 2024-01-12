package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.models.outputs.{Member, Permissions}

trait MemberService {
  def update(memberId: String, name: String, password: String): Option[Member]

  def permissions(memberId: String): Option[Permissions]
}
