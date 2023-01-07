package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.entities.{Workspace => WorkspaceEntity}

case class Workspace(id: String, name: String, slug: String)

object Workspace {
  def apply(entity: WorkspaceEntity): Workspace = {
    Workspace(entity.id, entity.name, entity.slug)
  }
}