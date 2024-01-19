package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.models.inputs.{UpdateMember, UpdateReport}
import jp.ijufumi.openreports.services.{LoginService, MemberService}

class MemberServlet @Inject() (loginService: LoginService, memberService: MemberService)
    extends PrivateAPIServletBase(loginService) {
  get("/status") {
    ok(member())
  }

  get("/permissions") {
    val _memberId = memberId()
    val _workspaceId = workspaceId()
    ok(memberService.permissions(_memberId, _workspaceId))
  }

  get("/logout") {
    val header = authorizationHeader()
    loginService.logout(header)
  }

  put("/update") {
    val requestParam = extractBody[UpdateMember]()
    val _member = member()
    ok(memberService.update(_member.id, requestParam.name, requestParam.password))
  }
}
