package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class UpdateWorkspaceMember(roleId: String)

object UpdateWorkspaceMember {
  implicit val validate: Validator[UpdateWorkspaceMember] = validator[UpdateWorkspaceMember] { param =>
    param.roleId is notEmpty
  }
}
