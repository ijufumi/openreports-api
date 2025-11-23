package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class CreateWorkspaceMember(memberId: String, roleId: String)

object CreateWorkspaceMember {
  implicit val validate: Validator[CreateWorkspaceMember] = validator[CreateWorkspaceMember] { param =>
    param.memberId is notEmpty
    param.roleId is notEmpty
  }
}
