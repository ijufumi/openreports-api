package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.LoginService
import org.scalatra._

class MembersServlet @Inject() (loginService: LoginService)
    extends PrivateAPIServletBase(loginService) {
  get("/status") {
    val authorizationHeader = authorizationHeader()
    val member = loginService.getMemberByToken(authorizationHeader)
    if (member.isEmpty) {
      unauthorized("Invalid api token")
    } else {
      ok(member.get)
    }
  }
  get("/logout") {
    val authorizationHeader = authorizationHeader()
    loginService.logout(authorizationHeader)
  }

}
