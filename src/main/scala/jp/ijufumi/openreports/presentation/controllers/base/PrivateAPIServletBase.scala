package jp.ijufumi.openreports.presentation.controllers.base

import org.scalatra.servlet.{FileUploadSupport, MultipartConfig}
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.presentation.models.responses.Member
import jp.ijufumi.openreports.services.LoginService

abstract class PrivateAPIServletBase(loginService: LoginService)
    extends APIServletBase
    with FileUploadSupport {
  configureMultipartHandling(MultipartConfig(maxFileSize = Some(Config.UPLOAD_FILE_MAX_SIZE)))

  before() {
    if (!isOptions && !skipAuthorization()) {
      val header = authorizationHeader()
      var member = loginService.verifyAuthorizationHeader(header)
      if (member.isEmpty) {
        val refreshToken = refreshTokenHeader()
        if (refreshToken.isEmpty) {
          halt(forbidden("API Token is invalid"))
        } else {
          val accessToken = loginService.generateAccessToken(refreshToken)
          if (accessToken.isEmpty) {
            halt(forbidden("API Token is invalid"))
          } else {
            response.setHeader(Config.API_TOKEN_HEADER, accessToken.get)
            member = loginService.verifyApiToken(accessToken.get)
            setMember(member.get)
            val refreshToken = loginService.generateRefreshToken(memberId())
            response.setHeader(Config.REFRESH_TOKEN_HEADER, refreshToken)
          }
        }
      } else {
        setMember(member.get)
      }
    }
  }

  def skipAuthorization(): Boolean = false

  def authorizationHeader(): String = {
    request.getHeader(Config.AUTHORIZATION_HEADER)
  }

  def refreshTokenHeader(): String = {
    request.getHeader(Config.REFRESH_TOKEN_HEADER)
  }

  def workspaceId(): String = {
    request.getHeader(Config.WORKSPACE_ID_HEADER)
  }

  def memberId(): String = {
    member().id
  }

  def member(): Member = {
    request.getAttribute(ATTRIBUTE_KEY_MEMBER).asInstanceOf[Member]
  }

  def memberOpt(): Option[Member] = {
    val _member = request.getAttribute(ATTRIBUTE_KEY_MEMBER)
    if (_member != null) {
      return Some(_member.asInstanceOf[Member])
    }
    None
  }
}
