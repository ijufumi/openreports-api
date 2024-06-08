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
      val member = loginService.verifyApiToken(header)
      if (member.isEmpty) {
        halt(forbidden("API Token is invalid"))
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
