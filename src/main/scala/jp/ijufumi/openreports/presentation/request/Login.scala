package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class Login(email: String, password: String)

object Login {
  implicit val validate: Validator[Login] = new Validator[Login] {
    def validate(param: Login) = collectViolations(
      notEmpty("email", param.email),
      notEmpty("password", param.password),
    )
  }
}
