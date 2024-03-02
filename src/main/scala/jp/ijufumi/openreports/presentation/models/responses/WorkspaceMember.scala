package jp.ijufumi.openreports.presentation.models.responses

case class WorkspaceMember(
    workspaceId: String,
    memberId: String,
    roleId: String,
    member: Option[Member] = None,
)
