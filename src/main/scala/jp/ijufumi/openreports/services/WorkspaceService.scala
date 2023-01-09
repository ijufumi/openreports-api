package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.entities.Workspace
import jp.ijufumi.openreports.models.inputs.UpdateWorkspace
import jp.ijufumi.openreports.models.outputs.WorkspaceMember

trait WorkspaceService {
  def createAndRelevant(memberId: String, email: String): Option[Workspace]

  def getWorkspace(id: String): Option[Workspace]

  def updateWorkspace(id: String, input: UpdateWorkspace): Option[Workspace]

  def getWorkspaceMembers(id: String): Seq[WorkspaceMember]


}
