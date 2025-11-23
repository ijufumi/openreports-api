package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class UpdateWorkspace (name: String)

object UpdateWorkspace {
  implicit val validate: Validator[UpdateWorkspace] = validator[UpdateWorkspace] { param =>
    param.name is notEmpty
    param.name.length is between(1, 255)
  }
}
