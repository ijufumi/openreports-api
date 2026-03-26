package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class UpdateWorkspace(name: String)

object UpdateWorkspace {
  implicit val validate: Validator[UpdateWorkspace] = new Validator[UpdateWorkspace] {
    def validate(param: UpdateWorkspace) = collectViolations(
      notEmpty("name", param.name),
      between("name", param.name.length, 1, 255),
    )
  }
}
