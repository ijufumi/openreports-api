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
      hookResult(Unauthorized("email or password or both are incorrect"))
    } else {
      hookResult(Ok(member.get))
    }
  }
  get("/google/authorization_url") {
    hookResult(Ok(GoogleAuthUrl(loginService.getAuthorizationUrl)))
  }

  post("/google") {
    val loginRequest = extractBody[GoogleLogin]()
    val member = loginService.loginWithGoogle(loginRequest.code)
    if (member.isEmpty) {
      hookResult(Unauthorized("state or code or both are invalid"))
    } else {
      hookResult(Ok(member.get))
    }
  }
}
