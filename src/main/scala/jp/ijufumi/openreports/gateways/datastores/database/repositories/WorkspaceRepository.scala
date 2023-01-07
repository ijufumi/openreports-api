package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.entities.Workspace

trait WorkspaceRepository {

  def getById(id: String): Option[Workspace]

  def getsByMemberId(memberId: String): Seq[Workspace]

  def register(workspace: Workspace): Option[Workspace]

  def update(workspace: Workspace): Unit
}