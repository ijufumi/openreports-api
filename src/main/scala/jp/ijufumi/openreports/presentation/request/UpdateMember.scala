package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.{ValidationResult, Validator}

case class UpdateMember(name: String, password: String)

object UpdateMember {
  implicit val validate: Validator[UpdateMember] = new Validator[UpdateMember] {
    def validate(param: UpdateMember): ValidationResult = collectViolations(
      notEmpty("name", param.name),
      between("name", param.name.length, 1, 255),
      // notEmpty("password", param.password), パスワード変更は任意項目のため、空文字チェックは不要です
    )
  }
}
