package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.models.inputs.{UpdateMember, UpdateReport}
import jp.ijufumi.openreports.services.{LoginService, MemberService}

class MemberServlet @Inject() (loginService: LoginService, memberService: MemberService)
    extends PrivateAPIServletBase(loginService) {
  get("/status") {
    val header = authorizationHeader()
    val member = loginService.getMemberByToken(header)
    if (member.isEmpty) {
      unauthorized("Invalid api token")
    } else {
      ok(member.get)
    }
  }
  get("/logout") {
    val header = authorizationHeader()
    loginService.logout(header)
  }
  put("/update") {
    val header = authorizationHeader()
    val memberOpt = loginService.getMemberByToken(header)
    if (memberOpt.isEmpty) {
      unauthorized("Invalid api token")
    } else {
      val requestParam = extractBody[UpdateMember]()
      ok(memberService.update(memberOpt.get.id, requestParam.name, requestParam.password))
    }
  }
}
