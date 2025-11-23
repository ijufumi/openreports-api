package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class GoogleLogin(code: String)

object GoogleLogin {
  implicit val validate: Validator[GoogleLogin] = validator[GoogleLogin] { param =>
    param.code is notEmpty
  }
}
