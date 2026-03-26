package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class UpdateMember(name: String, password: String)

object UpdateMember {
  implicit val validate: Validator[UpdateMember] = new Validator[UpdateMember] {
    def validate(param: UpdateMember) = collectViolations(
      notEmpty("name", param.name),
      between("name", param.name.length, 1, 255),
    )
  }
}
