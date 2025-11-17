package jp.ijufumi.openreports.presentation.response

case class WorkspaceMember(
    workspaceId: String,
    memberId: String,
    roleId: String,
    member: Option[Member] = None,
)
