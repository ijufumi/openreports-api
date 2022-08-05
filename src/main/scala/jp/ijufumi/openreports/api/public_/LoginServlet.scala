package jp.ijufumi.openreports.api.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.APIServletBase
import jp.ijufumi.openreports.services.LoginService
import jp.ijufumi.openreports.vo.request.{GoogleLoginRequest, LoginRequest}
import org.scalatra._

class LoginServlet @Inject() (loginService: LoginService) extends APIServletBase {
  post("/password") {
    val loginRequest = extractBody[LoginRequest]()
    val member = loginService.login(loginRequest.email, loginRequest.password)
    if (member.isEmpty) {
      Unauthorized("email or password or both are incorrect")
    } else {
      Ok(member.get)
    }
  }
  get("/google/authorization_url") {
    loginService.getAuthorizationUrl
  }

  post("/google") {
    val loginRequest = extractBody[GoogleLoginRequest]()
    val member = loginService.loginWithGoogle(loginRequest.state, loginRequest.code)
    if (member.isEmpty) {
      Unauthorized("state or code or both are invalid")
    } else {
      Ok(member.get)
    }
  }
}
