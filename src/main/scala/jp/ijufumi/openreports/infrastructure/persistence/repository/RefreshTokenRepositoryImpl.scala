package jp.ijufumi.openreports.infrastructure.persistence.repository

import scala.concurrent.Await
import jp.ijufumi.openreports.domain.repository.RefreshTokenRepository
import jp.ijufumi.openreports.domain.models.entity.RefreshToken
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.infrastructure.persistence.converter.RefreshTokenConverter.conversions._

class RefreshTokenRepositoryImpl extends RefreshTokenRepository {
  override def getByToken(db: Database, refreshToken: String): Option[RefreshToken] = {
    val getTokens = refreshTokenQuery
      .filter(_.refreshToken === refreshToken)
    val tokens = Await.result(db.run(getTokens.result), queryTimeout)
    if (tokens.isEmpty) {
      return None
    }
    Some(tokens.head)
  }

  override def register(db: Database, refreshToken: RefreshToken): Option[RefreshToken] = {
    val register = (refreshTokenQuery += refreshToken).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, refreshToken.id)
  }

  override def markUsed(db: Database, refreshToken: String, usedAt: Long): Int = {
    val query = refreshTokenQuery
      .filter(t => t.refreshToken === refreshToken && t.usedAt.isEmpty)
      .map(_.usedAt)
      .update(Some(usedAt))
      .withPinnedSession
    Await.result(db.run(query), queryTimeout)
  }

  override def deleteByToken(db: Database, refreshToken: String): Int = {
    val query = refreshTokenQuery.filter(_.refreshToken === refreshToken).delete.withPinnedSession
    Await.result(db.run(query), queryTimeout)
  }

  override def deleteByMemberId(db: Database, memberId: String): Unit = {
    val query = refreshTokenQuery.filter(_.memberId === memberId).delete.withPinnedSession
    Await.result(db.run(query), queryTimeout)
  }

  override def deleteExpired(db: Database, now: Long): Unit = {
    val query = refreshTokenQuery.filter(_.expiredAt < now).delete.withPinnedSession
    Await.result(db.run(query), queryTimeout)
  }

  private def getById(db: Database, id: String): Option[RefreshToken] = {
    val getTokens = refreshTokenQuery
      .filter(_.id === id)
    val tokens = Await.result(db.run(getTokens.result), queryTimeout)
    if (tokens.isEmpty) {
      return None
    }
    Some(tokens.head)
  }
}
