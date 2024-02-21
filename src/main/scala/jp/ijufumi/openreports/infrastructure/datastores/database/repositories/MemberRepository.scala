package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Member

trait MemberRepository {
  def getById(id: String): Option[Member]

  def getByGoogleId(googleId: String): Option[Member]

  def getMemberByEmail(emailAddress: String): Option[Member]

  def register(member: Member): Option[Member]

  def update(member: Member): Unit
}
