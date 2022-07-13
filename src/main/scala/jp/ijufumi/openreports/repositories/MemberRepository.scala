package jp.ijufumi.openreports.repositories

import jp.ijufumi.openreports.entities.Member

trait MemberRepository {
  def getMemberByEmail(emailAddress: String): Option[Member]
}
