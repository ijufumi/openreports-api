package jp.ijufumi.openreports.presentation.controllers.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controllers.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.models.requests.{GoogleLogin, Login}
import jp.ijufumi.openreports.presentation.models.responses.GoogleAuthUrl
import jp.ijufumi.openreports.services.LoginService

class LoginServlet @Inject() (loginService: LoginService)
    extends PrivateAPIServletBase(loginService) {

  override def skipAuthorization(): Boolean = true

  post("/password") {
    val loginRequest = extractBody[Login]()
    val member = loginService.login(loginRequest)
    if (member.isEmpty) {
      unauthorized("email or password or both are incorrect")
    } else {
      setMember(member.get)
      ok(member.get)
    }
  }
  get("/google/authorization_url") {
    ok(GoogleAuthUrl(loginService.getAuthorizationUrl))
  }

  post("/google") {
    val loginRequest = extractBody[GoogleLogin]()
    val member = loginService.loginWithGoogle(loginRequest)
    if (member.isEmpty) {
      unauthorized("state or code or both are invalid")
    } else {
      setMember(member.get)
      ok(member.get)
    }
  }
}
