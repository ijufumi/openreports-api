package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.usecase.port.input.param.{
  CreateWorkspaceMemberInput,
  UpdateWorkspaceInput,
  UpdateWorkspaceMemberInput,
}
import jp.ijufumi.openreports.domain.models.entity.{Lists, Workspace, WorkspaceMember}

trait WorkspaceUseCase {
  def createAndRelevant(name: String, memberId: String): Option[Workspace]

  def getWorkspace(id: String): Option[Workspace]

  def getWorkspacesByMemberId(memberId: String): Lists[Workspace]

  def updateWorkspace(id: String, input: UpdateWorkspaceInput): Option[Workspace]

  def getWorkspaceMembers(id: String): Lists[WorkspaceMember]

  def getWorkspaceMember(workspaceId: String, memberId: String): Option[WorkspaceMember]

  def createWorkspaceMember(
      workspaceId: String,
      input: CreateWorkspaceMemberInput,
  ): Option[WorkspaceMember]

  def updateWorkspaceMember(
      workspaceId: String,
      memberId: String,
      input: UpdateWorkspaceMemberInput,
  ): Option[WorkspaceMember]

  def deleteWorkspaceMember(workspaceId: String, memberId: String): Unit
}
