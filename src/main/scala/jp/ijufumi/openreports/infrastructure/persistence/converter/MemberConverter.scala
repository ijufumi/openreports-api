package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{Member => MemberModel, Workspace}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Member => MemberEntity}

object MemberConverter {
  def toDomain(entity: MemberEntity): MemberModel = {
    MemberModel(
      entity.id,
      entity.googleId,
      entity.email,
      entity.password,
      entity.name,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toDomain(entity: (MemberEntity, Seq[Workspace])): MemberModel = {
    MemberModel(
      entity._1.id,
      entity._1.googleId,
      entity._1.email,
      entity._1.password,
      entity._1.name,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      entity._2,
    )
  }

  def toEntity(model: MemberModel): MemberEntity = {
    MemberEntity(
      model.id,
      model.googleId,
      model.email,
      model.password,
      model.name,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def fromMemberEntity(entity: MemberEntity): MemberModel = toDomain(entity)
    implicit def fromMemberEntity2(entity: Option[MemberEntity]): Option[MemberModel] =
      entity.map(toDomain)
    implicit def fromMemberEntities(entity: Seq[MemberEntity]): Seq[MemberModel] =
      entity.map(toDomain)
    implicit def toMemberEntity(model: MemberModel): MemberEntity = toEntity(model)
  }
}
