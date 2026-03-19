package jp.ijufumi.openreports.infrastructure.persistence.repository

import scala.concurrent.Await
import jp.ijufumi.openreports.domain.repository.MemberRepository
import jp.ijufumi.openreports.domain.models.entity.Member
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.infrastructure.persistence.converter.MemberConverter.conversions._

class MemberRepositoryImpl extends MemberRepository {
  override def getById(db: Database, id: String): Option[Member] = {
    val getMembers = memberQuery
      .filter(_.id === id)
    val members = Await.result(db.run(getMembers.result), queryTimeout)
    if (members.isEmpty) {
      return None
    }
    Some(members.head)
  }

  override def getByGoogleId(db: Database, googleId: String): Option[Member] = {
    val getMembers = memberQuery
      .filter(_.googleId === googleId)
    val members = Await.result(db.run(getMembers.result), queryTimeout)
    if (members.isEmpty) {
      return None
    }
    Some(members.head)
  }

  override def getMemberByEmail(db: Database, email: String): Option[Member] = {
    val getMembers = memberQuery
      .filter(_.email === email)
    val members = Await.result(db.run(getMembers.result), queryTimeout)
    if (members.isEmpty) {
      return None
    }
    Some(members.head)
  }

  override def register(db: Database, member: Member): Option[Member] = {
    val register = (memberQuery += member).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, member.id)
  }

  override def update(db: Database, member: Member): Unit = {
    val query = memberQuery.insertOrUpdate(member).withPinnedSession
    Await.result(db.run(query), queryTimeout)
  }
}
