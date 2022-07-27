package jp.ijufumi.openreports.api.base

import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.services.LoginService
import org.scalatra._

abstract class PrivateAPIServletBase(loginService: LoginService) extends APIServletBase {
  before() {
    val apiToken = request.getHeader(Config.API_TOKEN_KEY)
    if (!loginService.verifyApiToken(apiToken)) {
      Forbidden("API Token is invalid")
    }
  }
}