package jp.ijufumi.openreports.interfaces.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{Workspace => WorkspaceEntity}
import jp.ijufumi.openreports.interfaces.models.inputs.UpdateWorkspace
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
