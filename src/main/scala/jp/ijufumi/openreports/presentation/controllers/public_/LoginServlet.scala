package jp.ijufumi.openreports.presentation.controllers.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.presentation.controllers.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.models.requests.{GoogleLogin, Login}
import jp.ijufumi.openreports.presentation.models.responses.GoogleAuthUrl
import jp.ijufumi.openreports.services.LoginService

class LoginServlet @Inject() (loginService: LoginService)
    extends PrivateAPIServletBase(loginService) {

  override def skipAuthorization(): Boolean = true

  post("/password") {
    val loginRequest = extractBody[Login]()
    val memberOpt = loginService.login(loginRequest)
    if (memberOpt.isEmpty) {
      unauthorized("email or password or both are incorrect")
    } else {
      val member = memberOpt.get
      setMember(member)
      setTokens(member.id)
      ok(member)
    }
  }
  get("/google/authorization_url") {
    ok(GoogleAuthUrl(loginService.getAuthorizationUrl))
  }

  post("/google") {
    val loginRequest = extractBody[GoogleLogin]()
    val memberOpt = loginService.loginWithGoogle(loginRequest)
    if (memberOpt.isEmpty) {
      unauthorized("state or code or both are invalid")
    } else {
      val member = memberOpt.get
      setMember(member)
      setTokens(member.id)
      ok(member)
    }
  }

  def setTokens(memberId: String): Unit = {
    val refreshToken = generateRefreshToken(memberId)
    generateAccessToken(refreshToken)
  }

  def generateRefreshToken(memberId: String): String = {
    val refreshToken = loginService.generateRefreshToken(memberId)
    response.setHeader(Config.REFRESH_TOKEN_HEADER, refreshToken)
    refreshToken
  }

  def generateAccessToken(refreshToken: String): Unit = {
    val accessToken = loginService.generateAccessToken(refreshToken)
    response.setHeader(Config.API_TOKEN_HEADER, accessToken.get)
  }
}
