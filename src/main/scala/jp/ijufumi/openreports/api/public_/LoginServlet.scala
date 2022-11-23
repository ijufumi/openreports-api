package jp.ijufumi.openreports.api.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.APIServletBase
import jp.ijufumi.openreports.services.LoginService
import jp.ijufumi.openreports.vo.request.{GoogleLogin, Login}
import jp.ijufumi.openreports.vo.response.GoogleAuthUrl
import org.scalatra._

class LoginServlet @Inject() (loginService: LoginService) extends APIServletBase {
  post("/password") {
    val loginRequest = extractBody[Login]()
    val member = loginService.login(loginRequest.email, loginRequest.password)
    if (member.isEmpty) {
      unauthorized("email or password or both are incorrect")
    } else {
      ok(member.get)
    }
  }
  get("/google/authorization_url") {
    ok(GoogleAuthUrl(loginService.getAuthorizationUrl))
  }

  post("/google") {
    val loginRequest = extractBody[GoogleLogin]()
    val member = loginService.loginWithGoogle(loginRequest.code)
    if (member.isEmpty) {
      unauthorized("state or code or both are invalid")
    } else {
      ok(member.get)
    }
  }
}
