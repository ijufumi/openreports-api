package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import scala.concurrent.Await
import queries.{memberQuery => query}
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.MemberRepository
import jp.ijufumi.openreports.domain.models.entity.Member
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.domain.models.entity.Member.conversions._

class MemberRepositoryImpl extends MemberRepository {
  override def getById(db: Database, id: String): Option[Member] = {
    val getMembers = query
      .filter(_.id === id)
    val members = Await.result(db.run(getMembers.result), queryTimeout)
    if (members.isEmpty) {
      return None
    }
    Some(members.head)
  }

  override def getByGoogleId(db: Database, googleId: String): Option[Member] = {
    val getMembers = query
      .filter(_.googleId === googleId)
    val members = Await.result(db.run(getMembers.result), queryTimeout)
    if (members.isEmpty) {
      return None
    }
    Some(members.head)
  }

  override def getMemberByEmail(db: Database, email: String): Option[Member] = {
    val getMembers = query
      .filter(_.email === email)
    val members = Await.result(db.run(getMembers.result), queryTimeout)
    if (members.isEmpty) {
      return None
    }
    Some(members.head)
  }

  override def register(db: Database, member: Member): Option[Member] = {
    val register = (query += member).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, member.id)
  }

  override def update(db: Database, member: Member): Unit = {
    query.insertOrUpdate(member).withPinnedSession
  }
}
