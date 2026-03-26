package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{Workspace => WorkspaceModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  Workspace => WorkspaceEntity,
  WorkspaceMember => WorkspaceMemberEntity,
}

object WorkspaceConverter {
  def toDomain(entity: WorkspaceEntity): WorkspaceModel = {
    WorkspaceModel(
      entity.id,
      entity.name,
      entity.slug,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toDomain(entity: (WorkspaceEntity, WorkspaceMemberEntity)): WorkspaceModel = {
    toDomain(entity._1)
  }

  def toEntity(model: WorkspaceModel): WorkspaceEntity = {
    WorkspaceEntity(
      model.id,
      model.name,
      model.slug,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toWorkspaceEntity(model: WorkspaceModel): WorkspaceEntity = toEntity(model)
    implicit def fromWorkspaceEntity(entity: WorkspaceEntity): WorkspaceModel = toDomain(entity)
    implicit def fromWorkspaceEntity2(entity: Option[WorkspaceEntity]): Option[WorkspaceModel] =
      entity.map(toDomain)
    implicit def fromWorkspaceEntity3(
        entity: (WorkspaceEntity, WorkspaceMemberEntity),
    ): WorkspaceModel = toDomain(entity)
    implicit def fromWorkspaceEntity4(
        entity: Option[(WorkspaceEntity, WorkspaceMemberEntity)],
    ): Option[WorkspaceModel] = entity.map(toDomain)
    implicit def fromWorkspaceEntities(entity: Seq[WorkspaceEntity]): Seq[WorkspaceModel] =
      entity.map(toDomain)
    implicit def fromWorkspaceEntities2(
        entity: Seq[(WorkspaceEntity, WorkspaceMemberEntity)],
    ): Seq[WorkspaceModel] = entity.map(toDomain)
  }
}
