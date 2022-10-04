package jp.ijufumi.openreports.api.base

import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.services.LoginService
import org.scalatra._

abstract class PrivateAPIServletBase(loginService: LoginService) extends APIServletBase {

  before() {
    val authorizationHeader = getAuthorizationHeader()
    if (!loginService.verifyApiToken(authorizationHeader)) {
      Forbidden("API Token is invalid")
    }
  }

  def getAuthorizationHeader(): String = {
    request.getHeader(Config.AUTHORIZATION_HEADER)
  }
}
