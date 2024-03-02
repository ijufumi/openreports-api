package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  Workspace => WorkspaceEntity,
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
}
