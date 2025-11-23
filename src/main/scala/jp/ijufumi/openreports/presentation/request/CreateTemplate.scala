package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class CreateTemplate(name: String)

object CreateTemplate {
  implicit val validate: Validator[CreateTemplate] = validator[CreateTemplate] { param =>
    param.name is notEmpty
    param.name.length is between(1, 255)
  }
}
