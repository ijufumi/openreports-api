package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  RefreshToken => RefreshTokenEntity,
}

case class RefreshToken(
    id: String,
    memberId: String,
    refreshToken: String,
    isUsed: Boolean,
    createdAt: Long,
    updatedAt: Long,
    version: Long,
) {
  def toEntity: RefreshTokenEntity = {
    RefreshTokenEntity(
      this.id,
      this.memberId,
      this.refreshToken,
      this.isUsed,
      this.createdAt,
      this.updatedAt,
      this.version,
    )
  }
}

object RefreshToken {
  def apply(entity: RefreshTokenEntity): RefreshToken = {
    RefreshToken(
      entity.id,
      entity.memberId,
      entity.refreshToken,
      entity.isUsed,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def fromRefreshTokenEntity(entity: RefreshTokenEntity): RefreshToken = {
      RefreshToken(entity)
    }

    implicit def toRefreshTokenEntity(model: RefreshToken): RefreshTokenEntity = {
      model.toEntity
    }
  }
}
