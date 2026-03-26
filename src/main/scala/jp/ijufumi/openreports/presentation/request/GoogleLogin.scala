package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class GoogleLogin(code: String)

object GoogleLogin {
  implicit val validate: Validator[GoogleLogin] = new Validator[GoogleLogin] {
    def validate(param: GoogleLogin) = collectViolations(
      notEmpty("code", param.code),
    )
  }
}
