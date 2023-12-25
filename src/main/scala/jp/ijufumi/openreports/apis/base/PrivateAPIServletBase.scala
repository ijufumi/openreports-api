package jp.ijufumi.openreports.apis.base

import org.scalatra.servlet.{FileUploadSupport, MultipartConfig}
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.models.outputs.Member
import jp.ijufumi.openreports.services.LoginService

abstract class PrivateAPIServletBase(loginService: LoginService)
    extends APIServletBase
    with FileUploadSupport {
  configureMultipartHandling(MultipartConfig(maxFileSize = Some(Config.UPLOAD_FILE_MAX_SIZE)))

  before() {
    val header = authorizationHeader()
    if (!loginService.verifyApiToken(header)) {
      halt(forbidden("API Token is invalid"))
    } else {
      val member = loginService.getMemberByToken(header, generateToken = false)
      request.setAttribute("member", member)
    }
  }

  after() {
    request.removeAttribute("member")
  }

  def authorizationHeader(): String = {
    request.getHeader(Config.AUTHORIZATION_HEADER)
  }

  def workspaceId(): String = {
    request.getHeader(Config.WORKSPACE_ID_HEADER)
  }

  def memberId(): String = {
    val member = request.getAttribute("name").asInstanceOf[Member]
    member.id
  }
}
