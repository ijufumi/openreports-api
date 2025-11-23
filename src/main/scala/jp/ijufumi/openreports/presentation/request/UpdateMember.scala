package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class UpdateMember(name: String, password: String)

object UpdateMember {
  implicit val validate: Validator[UpdateMember] = validator[UpdateMember] { param =>
    param.name is notEmpty
    param.name.length is between(1, 255)
  }
}
