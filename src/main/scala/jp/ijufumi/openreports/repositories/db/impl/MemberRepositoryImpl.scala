package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import jp.ijufumi.openreports.entities.{Member, Members}
import jp.ijufumi.openreports.repositories.db.MemberRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

class MemberRepositoryImpl @Inject() (db: Database) extends MemberRepository {
  private lazy val query = TableQuery[Members]

  override def getById(id: Int): Option[Member] = {
    val getMembers: Query[Members, Member, Seq] = query
      .filter(_.id === id)
    val members = Await.result(db.run(getMembers.result), Duration("10s"))
    if (members.isEmpty) {
      Option.empty[Member]
    }
    Option.apply(members.head)
  }

  override def getMemberByEmail(emailAddress: String): Option[Member] = {
    val getMembers: Query[Members, Member, Seq] = query
      .filter(_.emailAddress === emailAddress)
    val members = Await.result(db.run(getMembers.result), Duration("10s"))
    if (members.isEmpty) {
      Option.empty[Member]
    }
    Option.apply(members.head)
  }

  override def register(member: Member): Option[Member] = {
    val id = Await.result(db.run(query += member), Duration("1m"))
    getById(id)
  }
}
