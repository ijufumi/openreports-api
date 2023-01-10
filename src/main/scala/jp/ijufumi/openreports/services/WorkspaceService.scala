package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.entities.Workspace
import jp.ijufumi.openreports.models.inputs.{CreateWorkspaceMember, UpdateWorkspace, UpdateWorkspaceMember}
import jp.ijufumi.openreports.models.outputs.WorkspaceMember

trait WorkspaceService {
  def createAndRelevant(memberId: String, email: String): Option[Workspace]

  def getWorkspace(id: String): Option[Workspace]

  def updateWorkspace(id: String, input: UpdateWorkspace): Option[Workspace]

  def getWorkspaceMembers(id: String): Seq[WorkspaceMember]

  def getWorkspaceMember(workspaceId: String, memberId: String): Option[WorkspaceMember]

  def createWorkspaceMember(workspaceId: String, input: CreateWorkspaceMember): Option[WorkspaceMember]

  def updateWorkspaceMember(workspaceId: String, memberId: String, input: UpdateWorkspaceMember): Option[WorkspaceMember]

  def deleteWorkspaceMember(workspaceId: String, memberId: String): Unit
}
