package jp.ijufumi.openreports.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.{Member, Members}
import jp.ijufumi.openreports.repositories._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

@Inject
class MemberRepositoryImpl(db: Database) extends MemberRepository {
  private lazy val query = TableQuery[Members]

  def getMemberByEmail(emailAddress: String): Option[Member] = {
    val members: Query[Members, Member, Seq] = query
      .filter(_.emailAddress === emailAddress)
    val f: Future[Seq[Member]] = db.run(members.result)
    Option.empty[Member]
  }
}
