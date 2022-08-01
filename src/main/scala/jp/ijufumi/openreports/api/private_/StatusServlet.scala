package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.LoginService
import org.scalatra._

class StatusServlet @Inject() (loginService: LoginService)
    extends PrivateAPIServletBase(loginService) {
  get("/status") {
    val apiToken = getApiToken()
    val member = loginService.getMemberByToken(apiToken)
    if (member.isEmpty) {
      Unauthorized("Invalid api token")
    } else {
      Ok(member.get)
    }
  }
}
