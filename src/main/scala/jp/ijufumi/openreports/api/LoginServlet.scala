package jp.ijufumi.openreports.api

import com.google.inject.Inject
import jp.ijufumi.openreports.services.LoginService
import jp.ijufumi.openreports.vo.request.LoginRequest
import org.scalatra._

class LoginServlet @Inject() (loginService: LoginService) extends APIServletBase {
  post("/") {
    val request = parsedBody.extract[LoginRequest]
    loginService.login(request.email, request.password)
    Ok("hello")
  }
}
