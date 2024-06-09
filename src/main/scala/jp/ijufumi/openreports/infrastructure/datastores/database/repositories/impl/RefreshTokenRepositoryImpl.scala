package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.domain.models.entity.RefreshToken
import jp.ijufumi.openreports.domain.models.entity.RefreshToken.conversions._
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.RefreshTokenRepository
import slick.jdbc
import slick.jdbc.JdbcBackend
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class RefreshTokenRepositoryImpl extends RefreshTokenRepository {

  override def create(
      db: JdbcBackend.Database,
      refreshToken: RefreshToken,
  ): Option[RefreshToken] = {
    refreshTokenQuery.insertOrUpdate(refreshToken)
    getById(db, refreshToken.id)
  }

  override def update(
      db: JdbcBackend.Database,
      refreshToken: RefreshToken,
  ): Option[RefreshToken] = {
    refreshTokenQuery.insertOrUpdate(refreshToken)
    getById(db, refreshToken.id)
  }

  override def delete(db: JdbcBackend.Database, id: String): Int = {
    val query = refreshTokenQuery.filter(_.id === id)
    Await.result(db.run(query.delete), queryTimeout)
  }

  override def deleteByToken(db: jdbc.JdbcBackend.Database, token: String): Int = {
    val query = refreshTokenQuery.filter(_.refreshToken === token)
    Await.result(db.run(query.delete), queryTimeout)
  }

  override def deleteByMemberId(
      db: _root_.slick.jdbc.JdbcBackend.DatabaseDef,
      memberId: String,
  ): Int = {
    val query = refreshTokenQuery.filter(_.memberId === memberId)
    Await.result(db.run(query.delete), queryTimeout)
  }

  override def getById(db: JdbcBackend.Database, id: String): Option[RefreshToken] = {
    val query = refreshTokenQuery.filter(_.id === id).filter(!_.isUsed)
    val refreshTokens = Await.result(db.run(query.result), queryTimeout)

    if (refreshTokens.isEmpty) {
      return None
    }

    Some(refreshTokens.head)
  }

  override def getByMemberId(db: JdbcBackend.Database, memberId: String): Option[RefreshToken] = {
    val query = refreshTokenQuery.filter(_.memberId === memberId).filter(!_.isUsed)
    val refreshTokens = Await.result(db.run(query.result), queryTimeout)

    if (refreshTokens.isEmpty) {
      return None
    }

    Some(refreshTokens.head)
  }

  override def getByToken(db: JdbcBackend.Database, token: String): Option[RefreshToken] = {
    val query = refreshTokenQuery.filter(_.refreshToken === token).filter(!_.isUsed)
    val refreshTokens = Await.result(db.run(query.result), queryTimeout)

    if (refreshTokens.isEmpty) {
      return None
    }

    Some(refreshTokens.head)
  }
}
