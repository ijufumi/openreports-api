package jp.ijufumi.openreports.presentation.controller.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.presentation.controller.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.request.{GoogleLogin, Login}
import jp.ijufumi.openreports.presentation.response.GoogleAuthUrl
import jp.ijufumi.openreports.presentation.converter.MemberConverter.conversions._
import jp.ijufumi.openreports.presentation.converter.LoginConverter
import jp.ijufumi.openreports.usecase.port.input.LoginUseCase

class LoginServlet @Inject() (loginService: LoginUseCase)
    extends PrivateAPIServletBase(loginService) {

  override def skipAuthorization(): Boolean = true

  post("/password") {
    validateBody[Login] { validatedRequest =>
      {
        val memberOpt = loginService.login(LoginConverter.toLoginInput(validatedRequest))
        if (memberOpt.isEmpty) {
          unauthorized("email or password or both are incorrect")
        } else {
          val member = memberOpt.get
          setMember(member)
          setTokens(member.id)
          ok(member)
        }
      }
    }
  }
  get("/google/authorization_url") {
    ok(GoogleAuthUrl(loginService.getAuthorizationUrl))
  }

  post("/google") {
    validateBody[GoogleLogin] { validatedRequest =>
      {
        val memberOpt =
          loginService.loginWithGoogle(LoginConverter.toGoogleLoginInput(validatedRequest))
        if (memberOpt.isEmpty) {
          unauthorized("state or code or both are invalid")
        } else {
          val member = memberOpt.get
          setMember(member)
          setTokens(member.id)
          ok(member)
        }
      }
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
