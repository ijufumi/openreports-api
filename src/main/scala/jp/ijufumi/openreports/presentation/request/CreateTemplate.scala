package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class CreateTemplate(name: String)

object CreateTemplate {
  implicit val validate: Validator[CreateTemplate] = new Validator[CreateTemplate] {
    def validate(param: CreateTemplate) = collectViolations(
      notEmpty("name", param.name),
      between("name", param.name.length, 1, 255),
    )
  }
}
