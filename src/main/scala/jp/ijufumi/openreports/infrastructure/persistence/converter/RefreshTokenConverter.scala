package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{RefreshToken => RefreshTokenModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{RefreshToken => RefreshTokenEntity}

object RefreshTokenConverter {
  def toDomain(entity: RefreshTokenEntity): RefreshTokenModel = {
    RefreshTokenModel(
      entity.id,
      entity.memberId,
      entity.refreshToken,
      entity.expiredAt,
      entity.usedAt,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toEntity(model: RefreshTokenModel): RefreshTokenEntity = {
    RefreshTokenEntity(
      model.id,
      model.memberId,
      model.refreshToken,
      model.expiredAt,
      model.usedAt,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def fromRefreshTokenEntity(entity: RefreshTokenEntity): RefreshTokenModel =
      toDomain(entity)
    implicit def fromRefreshTokenEntity2(
        entity: Option[RefreshTokenEntity],
    ): Option[RefreshTokenModel] =
      entity.map(toDomain)
    implicit def fromRefreshTokenEntities(entity: Seq[RefreshTokenEntity]): Seq[RefreshTokenModel] =
      entity.map(toDomain)
    implicit def toRefreshTokenEntity(model: RefreshTokenModel): RefreshTokenEntity =
      toEntity(model)
  }
}
