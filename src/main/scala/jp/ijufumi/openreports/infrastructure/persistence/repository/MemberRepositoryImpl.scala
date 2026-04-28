package jp.ijufumi.openreports.infrastructure.persistence.repository

import scala.concurrent.Await
import jp.ijufumi.openreports.domain.repository.MemberRepository
import jp.ijufumi.openreports.domain.models.entity.Member
import jp.ijufumi.openreports.exceptions.OptimisticLockException
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.infrastructure.persistence.converter.MemberConverter.conversions._
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Member => MemberEntity}

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
    val newEntity: MemberEntity =
      member.copy(updatedAt = Dates.currentTimestamp(), versions = member.versions + 1)
    val q = memberQuery
      .filter(_.id === member.id)
      .filter(_.versions === member.versions)
    val affected = Await.result(db.run(q.update(newEntity).withPinnedSession), queryTimeout)
    if (affected == 0) {
      throw new OptimisticLockException(
        s"Member id=${member.id} was modified concurrently or does not exist",
      )
    }
  }

  override def delete(db: Database, id: String): Unit = {
    val query = memberQuery.filter(_.id === id).delete.withPinnedSession
    Await.result(db.run(query), queryTimeout)
  }
}
