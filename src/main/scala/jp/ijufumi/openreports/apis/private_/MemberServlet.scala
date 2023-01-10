package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.LoginService

class MemberServlet @Inject()(loginService: LoginService)
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

}
