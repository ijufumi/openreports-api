package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class CreateWorkspaceMember(memberId: String, roleId: String)

object CreateWorkspaceMember {
  implicit val validate: Validator[CreateWorkspaceMember] = new Validator[CreateWorkspaceMember] {
    def validate(param: CreateWorkspaceMember) = collectViolations(
      notEmpty("memberId", param.memberId),
      notEmpty("roleId", param.roleId),
    )
  }
}
