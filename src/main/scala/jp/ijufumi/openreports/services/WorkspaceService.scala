package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.presentation.models.requests.{CreateWorkspace, CreateWorkspaceMember, UpdateWorkspace, UpdateWorkspaceMember}
import jp.ijufumi.openreports.presentation.models.responses.{Lists, Workspace, WorkspaceMember}

trait WorkspaceService {
  def createAndRelevant(input: CreateWorkspace, memberId: String): Option[Workspace]

  def createAndRelevant(name: String, memberId: String): Option[Workspace]

  def getWorkspace(id: String): Option[Workspace]

  def getWorkspacesByMemberId(memberId: String): Lists[Workspace]

  def updateWorkspace(id: String, input: UpdateWorkspace): Option[Workspace]

  def getWorkspaceMembers(id: String): Lists[WorkspaceMember]

  def getWorkspaceMember(workspaceId: String, memberId: String): Option[WorkspaceMember]

  def createWorkspaceMember(workspaceId: String, input: CreateWorkspaceMember): Option[WorkspaceMember]

  def updateWorkspaceMember(workspaceId: String, memberId: String, input: UpdateWorkspaceMember): Option[WorkspaceMember]

  def deleteWorkspaceMember(workspaceId: String, memberId: String): Unit
}
