package jp.ijufumi.openreports.api

import com.google.inject.Inject
import jp.ijufumi.openreports.services.LoginService
import jp.ijufumi.openreports.vo.request.LoginRequest
import org.scalatra._

class LoginServlet @Inject() (loginService: LoginService) extends APIServletBase {
  post("/") {
    val request = parsedBody.extract[LoginRequest]
    val member = loginService.login(request.email, request.password)
    if (member.isEmpty) {
      Unauthorized("email or password or both are incorrect")
    } else {
      Ok(member.get)
    }
  }
}
