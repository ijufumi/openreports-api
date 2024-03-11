package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  Workspace => WorkspaceEntity,
  WorkspaceMember => WorkspaceMemberEntity,
}
import jp.ijufumi.openreports.presentation.models.responses.{Workspace => WorkspaceResponse}
import jp.ijufumi.openreports.presentation.models.requests.UpdateWorkspace
import jp.ijufumi.openreports.utils.Dates

case class Workspace(
    id: String,
    name: String,
    slug: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: WorkspaceEntity = {
    WorkspaceEntity(
      this.id,
      this.name,
      this.slug,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def toResponse: WorkspaceResponse = {
    WorkspaceResponse(
      this.id,
      this.name,
      this.slug,
    )
  }
  def mergeForUpdate(input: UpdateWorkspace): Workspace = {
    Workspace(
      this.id,
      input.name,
      this.slug,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
}

object Workspace {
  def apply(entity: WorkspaceEntity): Workspace = {
    Workspace(
      entity.id,
      entity.name,
      entity.slug,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def apply(entity: (WorkspaceEntity, WorkspaceMemberEntity)): Workspace = {
    Workspace(
      entity._1.id,
      entity._1.name,
      entity._1.slug,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toWorkspaceEntity(model: Workspace): WorkspaceEntity = {
      model.toEntity
    }

    implicit def fromWorkspaceEntity(entity: WorkspaceEntity): Workspace = {
      Workspace(entity)
    }

    implicit def fromWorkspaceEntity2(entity: Option[WorkspaceEntity]): Option[Workspace] = {
      entity.map(e => Workspace(e))
    }

    implicit def fromWorkspaceEntity3(entity: (WorkspaceEntity, WorkspaceMemberEntity)): Workspace = {
      Workspace(entity)
    }

    implicit def fromWorkspaceEntity4(entity: Option[(WorkspaceEntity, WorkspaceMemberEntity)]): Option[Workspace] = {
      entity.map(e => Workspace(e))
    }

    implicit def fromWorkspaceEntities(entity: Seq[WorkspaceEntity]): Seq[Workspace] = {
      entity.map(e => Workspace(e))
    }

    implicit def fromWorkspaceEntities2(entity: Seq[(WorkspaceEntity, WorkspaceMemberEntity)]): Seq[Workspace] = {
      entity.map(e => Workspace(e))
    }

    implicit def toWorkspaceResponse(model: Workspace): WorkspaceResponse = {
      model.toResponse
    }

    implicit def toWorkspaceResponse2(model: Option[Workspace]): Option[WorkspaceResponse] = {
      model.map(m => m.toResponse)
    }

    implicit def toWorkspaceResponses(model: Seq[Workspace]): Seq[WorkspaceResponse] = {
      model.map(m => m.toResponse)
    }
  }
}
