package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class CreateWorkspace(name: String)

object CreateWorkspace {
  implicit val validate: Validator[CreateWorkspace] = new Validator[CreateWorkspace] {
    def validate(param: CreateWorkspace) = collectViolations(
      notEmpty("name", param.name),
      between("name", param.name.length, 1, 255),
    )
  }
}
