package jp.ijufumi.openreports.presentation.converter

import jp.ijufumi.openreports.domain.models.entity.{
  Function => FunctionModel,
  Member => MemberModel,
  Permission => PermissionModel,
  Workspace => WorkspaceModel,
}
import jp.ijufumi.openreports.presentation.response.{
  Function => FunctionResponse,
  Member => MemberResponse,
  Permission => PermissionResponse,
  Workspace => WorkspaceResponse,
}

object MemberConverter {
  def toResponse(model: MemberModel): MemberResponse = {
    MemberResponse(
      model.id,
      model.email,
      model.name,
      model.workspaces.map(WorkspaceConverter.toResponse),
    )
  }

  def toPermissionResponse(model: PermissionModel): PermissionResponse = {
    PermissionResponse(
      model.workspaces.map(WorkspaceConverter.toResponse),
      model.functions.map(FunctionConverter.toResponse),
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toMemberResponse(model: MemberModel): MemberResponse = toResponse(model)
    implicit def toMemberResponse2(model: Option[MemberModel]): Option[MemberResponse] =
      model.map(toResponse)
    implicit def toMemberResponses(model: Seq[MemberModel]): Seq[MemberResponse] =
      model.map(toResponse)
  }
}
