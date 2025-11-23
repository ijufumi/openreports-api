package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class CreateWorkspace(name: String)

object CreateWorkspace {
  implicit val validate: Validator[CreateWorkspace] = validator[CreateWorkspace] { param =>
    param.name is notEmpty
    param.name.length is between(1, 255)
  }
}
