package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Member
import slick.jdbc.JdbcBackend.Database

trait MemberRepository {
  def getById(db: Database, id: String): Option[Member]

  def getByGoogleId(db: Database, googleId: String): Option[Member]

  def getMemberByEmail(db: Database, emailAddress: String): Option[Member]

  def register(db: Database, member: Member): Option[Member]

  def update(db: Database, member: Member): Unit
}
