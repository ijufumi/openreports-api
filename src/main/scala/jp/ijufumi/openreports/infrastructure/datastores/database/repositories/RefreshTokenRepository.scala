package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.RefreshToken
import slick.jdbc.JdbcBackend.Database

trait RefreshTokenRepository {
  def create(db: Database, refreshToken: RefreshToken): Option[RefreshToken]
  def update(db: Database, refreshToken: RefreshToken): Option[RefreshToken]
  def delete(db: Database, id: String): Int
  def getById(db: Database, id: String): Option[RefreshToken]
  def getByMemberId(db: Database, memberId: String): Option[RefreshToken]
  def getByToken(db: Database, token: String): Option[RefreshToken]
}
