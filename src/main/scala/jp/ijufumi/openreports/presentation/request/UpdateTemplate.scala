package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class UpdateTemplate(name: String)

object UpdateTemplate {
  implicit val validate: Validator[UpdateTemplate] = new Validator[UpdateTemplate] {
    def validate(param: UpdateTemplate) = collectViolations(
      notEmpty("name", param.name),
      lengthBetween("name", param.name, 1, 255),
    )
  }
}
