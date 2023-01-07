package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import jp.ijufumi.openreports.entities.Member
import jp.ijufumi.openreports.entities.queries.{memberQuery => query}
import jp.ijufumi.openreports.gateways.datastores.database.repositories.MemberRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

class MemberRepositoryImpl @Inject() (db: Database) extends MemberRepository {
  override def getById(id: String): Option[Member] = {
    val getMembers = query
      .filter(_.id === id)
    val members = Await.result(db.run(getMembers.result), queryTimeout)
    if (members.isEmpty) {
      return None
    }
    Some(members.head)
  }

  override def getByGoogleId(googleId: String): Option[Member] = {
    val getMembers = query
      .filter(_.googleId === googleId)
    val members = Await.result(db.run(getMembers.result), queryTimeout)
    if (members.isEmpty) {
      return None
    }
    Some(members.head)
  }

  override def getMemberByEmail(email: String): Option[Member] = {
    val getMembers = query
      .filter(_.email === email)
    val members = Await.result(db.run(getMembers.result), queryTimeout)
    if (members.isEmpty) {
      return None
    }
    Some(members.head)
  }

  override def register(member: Member): Option[Member] = {
    val register = (query += member).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(member.id)
  }

  override def update(member: Member): Unit = {
    query.insertOrUpdate(member).withPinnedSession
  }
}
