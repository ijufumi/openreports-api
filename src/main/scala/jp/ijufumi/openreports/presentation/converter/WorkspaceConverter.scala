package jp.ijufumi.openreports.presentation.converter

import jp.ijufumi.openreports.domain.models.entity.{
  Workspace => WorkspaceModel,
  WorkspaceMember => WorkspaceMemberModel,
}
import jp.ijufumi.openreports.presentation.response.{
  Workspace => WorkspaceResponse,
  WorkspaceMember => WorkspaceMemberResponse,
}
import jp.ijufumi.openreports.presentation.request.{UpdateWorkspace, CreateWorkspaceMember, UpdateWorkspaceMember}
import jp.ijufumi.openreports.usecase.port.input.param.{
  UpdateWorkspaceInput,
  CreateWorkspaceMemberInput,
  UpdateWorkspaceMemberInput,
}

object WorkspaceConverter {
  def toResponse(model: WorkspaceModel): WorkspaceResponse = {
    WorkspaceResponse(model.id, model.name, model.slug)
  }

  def toMemberResponse(model: WorkspaceMemberModel): WorkspaceMemberResponse = {
    WorkspaceMemberResponse(
      model.workspaceId,
      model.memberId,
      model.roleId,
      model.member.map(MemberConverter.toResponse),
    )
  }

  def toUpdateWorkspaceInput(request: UpdateWorkspace): UpdateWorkspaceInput = {
    UpdateWorkspaceInput(request.name)
  }

  def toCreateMemberInput(request: CreateWorkspaceMember): CreateWorkspaceMemberInput = {
    CreateWorkspaceMemberInput(request.memberId, request.roleId)
  }

  def toUpdateMemberInput(request: UpdateWorkspaceMember): UpdateWorkspaceMemberInput = {
    UpdateWorkspaceMemberInput(request.roleId)
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toWorkspaceResponse(model: WorkspaceModel): WorkspaceResponse = toResponse(model)
    implicit def toWorkspaceResponse2(model: Option[WorkspaceModel]): Option[WorkspaceResponse] = model.map(toResponse)
    implicit def toWorkspaceResponses(model: Seq[WorkspaceModel]): Seq[WorkspaceResponse] = model.map(toResponse)
    implicit def toWMResponse(model: WorkspaceMemberModel): WorkspaceMemberResponse = toMemberResponse(model)
    implicit def toWMResponse2(model: Option[WorkspaceMemberModel]): Option[WorkspaceMemberResponse] = model.map(toMemberResponse)
    implicit def toWMResponses(model: Seq[WorkspaceMemberModel]): Seq[WorkspaceMemberResponse] = model.map(toMemberResponse)
  }
}
