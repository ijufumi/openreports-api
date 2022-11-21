package jp.ijufumi.openreports.api.base

import org.scalatra._
import org.scalatra.servlet.{FileUploadSupport, MultipartConfig}
import jp.ijufumi.openreports.config.Config
import jp.ijufumi.openreports.services.LoginService

abstract class PrivateAPIServletBase(loginService: LoginService)
    extends APIServletBase
    with FileUploadSupport {
  configureMultipartHandling(MultipartConfig(maxFileSize = Some(Config.UPLOAD_FILE_MAX_SIZE)))

  before() {
    val authorizationHeader = getAuthorizationHeader()
    if (!loginService.verifyApiToken(authorizationHeader)) {
      Forbidden("API Token is invalid")
    }
  }

  def getAuthorizationHeader(): String = {
    request.getHeader(Config.AUTHORIZATION_HEADER)
  }

  def getWorkspaceId(): String = {
    request.getHeader(Config.WORKSPACE_ID_HEADER)
  }
}
