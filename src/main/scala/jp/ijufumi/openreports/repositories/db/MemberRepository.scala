package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.Member
import slick.dbio.{DBIOAction, Effect, NoStream}

trait MemberRepository {
  def getById(id: String): Option[Member]

  def getByGoogleId(googleId: String): Option[Member]

  def getMemberByEmail(emailAddress: String): Option[Member]

  def register(member: Member): Option[Member]

  def registerTransactional(member: Member): DBIOAction[Int, NoStream, Effect.Write]

  def update(member: Member): Unit
}
