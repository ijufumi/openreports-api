package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class Login(email: String, password: String)

object Login {
  implicit val validate: Validator[Login] = validator[Login] { param =>
    param.email is notEmpty
    // param.email is validEmail
    param.password is notEmpty
  }
}
