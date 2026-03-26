package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{WorkspaceMember => WorkspaceMemberModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  Member => MemberEntity,
  WorkspaceMember => WorkspaceMemberEntity,
}

object WorkspaceMemberConverter {
  def toDomain(entity: WorkspaceMemberEntity): WorkspaceMemberModel = {
    WorkspaceMemberModel(
      entity.workspaceId,
      entity.memberId,
      entity.roleId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toDomain(entity: (WorkspaceMemberEntity, MemberEntity)): WorkspaceMemberModel = {
    WorkspaceMemberModel(
      entity._1.workspaceId,
      entity._1.memberId,
      entity._1.roleId,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      Some(MemberConverter.toDomain(entity._2)),
    )
  }

  def toEntity(model: WorkspaceMemberModel): WorkspaceMemberEntity = {
    WorkspaceMemberEntity(
      model.workspaceId,
      model.memberId,
      model.roleId,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toWorkspaceMemberEntity(model: WorkspaceMemberModel): WorkspaceMemberEntity =
      toEntity(model)
    implicit def fromWorkspaceMemberEntity(entity: WorkspaceMemberEntity): WorkspaceMemberModel =
      toDomain(entity)
    implicit def fromWorkspaceMemberEntity2(
        entity: Option[WorkspaceMemberEntity],
    ): Option[WorkspaceMemberModel] = entity.map(toDomain)
    implicit def fromWorkspaceMemberEntity3(
        entity: (WorkspaceMemberEntity, MemberEntity),
    ): WorkspaceMemberModel = toDomain(entity)
    implicit def fromWorkspaceMemberEntity4(
        entity: Option[(WorkspaceMemberEntity, MemberEntity)],
    ): Option[WorkspaceMemberModel] = entity.map(toDomain)
    implicit def fromWorkspaceMemberEntities(
        entity: Seq[WorkspaceMemberEntity],
    ): Seq[WorkspaceMemberModel] = entity.map(toDomain)
    implicit def fromWorkspaceMemberEntities2(
        entity: Seq[(WorkspaceMemberEntity, MemberEntity)],
    ): Seq[WorkspaceMemberModel] = entity.map(toDomain)
  }
}
