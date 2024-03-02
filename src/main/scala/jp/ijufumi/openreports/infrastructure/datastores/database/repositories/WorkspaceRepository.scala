package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Workspace

trait WorkspaceRepository {

  def getById(id: String): Option[Workspace]

  def getsByMemberId(memberId: String): Seq[Workspace]

  def register(workspace: Workspace): Option[Workspace]

  def update(workspace: Workspace): Option[Workspace]
}
