package jp.ijufumi.openreports.domain.repository

import jp.ijufumi.openreports.domain.models.entity.RefreshToken
import slick.jdbc.JdbcBackend.Database

trait RefreshTokenRepository {
  def getByToken(db: Database, refreshToken: String): Option[RefreshToken]

  def register(db: Database, refreshToken: RefreshToken): Option[RefreshToken]

  def markUsed(db: Database, refreshToken: String, usedAt: Long): Int

  def deleteByToken(db: Database, refreshToken: String): Int

  def deleteByMemberId(db: Database, memberId: String): Unit

  def deleteExpired(db: Database, now: Long): Unit
}
