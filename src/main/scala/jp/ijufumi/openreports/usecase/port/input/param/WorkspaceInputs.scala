package jp.ijufumi.openreports.usecase.port.input.param

case class UpdateWorkspaceInput(name: String)

case class CreateWorkspaceMemberInput(memberId: String, roleId: String)

case class UpdateWorkspaceMemberInput(roleId: String)
