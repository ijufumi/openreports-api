package jp.ijufumi.openreports.api.base

import org.scalatra.servlet.{FileUploadSupport, MultipartConfig}
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.services.LoginService

abstract class PrivateAPIServletBase(loginService: LoginService)
    extends APIServletBase
    with FileUploadSupport {
  configureMultipartHandling(MultipartConfig(maxFileSize = Some(Config.UPLOAD_FILE_MAX_SIZE)))

  before() {
    val header = authorizationHeader()
    if (!loginService.verifyApiToken(header)) {
      forbidden("API Token is invalid")
    }
  }

  def authorizationHeader(): String = {
    request.getHeader(Config.AUTHORIZATION_HEADER)
  }

  def workspaceId(): String = {
    request.getHeader(Config.WORKSPACE_ID_HEADER)
  }
}
