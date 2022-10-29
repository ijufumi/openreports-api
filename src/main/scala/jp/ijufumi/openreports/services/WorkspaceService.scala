package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.entities.Workspace

trait WorkspaceService {
  def createAndRelevant(memberId: String, email: String): Option[Workspace]
}
