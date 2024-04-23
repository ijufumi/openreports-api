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
    if (!skipAuthorization()) {
      val header = authorizationHeader()
      if (!loginService.verifyApiToken(header)) {
        halt(forbidden("API Token is invalid"))
      } else {
        val member = loginService.getMemberByToken(header, generateToken = false)
        request.setAttribute("member", member)
      }
    }
  }

  after() {
    val _member = memberOpt()
    if (_member.isDefined) {
      val apiToken = loginService.generateApiToken(_member.get.id)
      request.removeAttribute("member")
      response.setHeader(Config.API_TOKEN_HEADER, apiToken)
    }
  }

  def skipAuthorization(): Boolean = false

  def authorizationHeader(): String = {
    request.getHeader(Config.AUTHORIZATION_HEADER)
  }

  def workspaceId(): String = {
    request.getHeader(Config.WORKSPACE_ID_HEADER)
  }

  def memberId(): String = {
    member().id
  }

  def member(): Member = {
    request.getAttribute("name").asInstanceOf[Member]
  }

  def memberOpt(): Option[Member] = {
    val _member = request.getAttribute("member")
    if (_member != null) {
      return Some(_member.asInstanceOf[Member])
    }
    None
  }
}
