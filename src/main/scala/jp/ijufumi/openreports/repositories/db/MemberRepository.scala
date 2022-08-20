package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.Member

trait MemberRepository {
  def getById(id: String): Option[Member]

  def getByGoogleId(googleId: String): Option[Member]

  def getMemberByEmail(emailAddress: String): Option[Member]

  def register(member: Member): Option[Member]

  def update(member: Member): Unit
}
