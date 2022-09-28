package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.LoginService
import org.scalatra._

class MembersServlet @Inject()(loginService: LoginService)
    extends PrivateAPIServletBase(loginService) {
  get("/status") {
    val apiToken = getApiToken()
    val member = loginService.getMemberByToken(apiToken)
    if (member.isEmpty) {
      hookResult(Unauthorized("Invalid api token"))
    } else {
      hookResult(Ok(member.get))
    }
  }
  get("/logout") {
    val apiToken = getApiToken()
    loginService.logout(apiToken)
  }

}
