package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class UpdateWorkspaceMember(roleId: String)

object UpdateWorkspaceMember {
  implicit val validate: Validator[UpdateWorkspaceMember] = new Validator[UpdateWorkspaceMember] {
    def validate(param: UpdateWorkspaceMember) = collectViolations(
      notEmpty("roleId", param.roleId),
    )
  }
}
