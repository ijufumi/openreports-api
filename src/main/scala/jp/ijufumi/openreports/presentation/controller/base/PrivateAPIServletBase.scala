package jp.ijufumi.openreports.presentation.controller.base

import com.wix.accord.Validator
import org.scalatra.servlet.{FileUploadSupport, MultipartConfig}
import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.presentation.response.Member
import jp.ijufumi.openreports.usecase.port.input.LoginUseCase
import org.apache.commons.lang3.StringUtils

abstract class PrivateAPIServletBase(loginService: LoginUseCase)
    extends APIServletBase
    with FileUploadSupport {
  configureMultipartHandling(MultipartConfig(maxFileSize = Some(Config.UPLOAD_FILE_MAX_SIZE)))

  before() {
    if (!isOptions && !skipAuthorization()) {
      val header = authorizationHeader()
      var member = loginService.verifyAuthorizationHeader(header)
      if (member.isEmpty) {
        val refreshToken = refreshTokenHeader()
        if (refreshToken == null || refreshToken.isEmpty) {
          halt(unauthorized("API Token is invalid"))
        } else {
          val accessToken = loginService.generateAccessToken(refreshToken)
          if (accessToken.isEmpty) {
            halt(unauthorized("API Token is invalid"))
          } else {
            response.setHeader(Config.API_TOKEN_HEADER, accessToken.get)
            member = loginService.verifyApiToken(accessToken.get)
            if (member.isEmpty) {
              halt(unauthorized("API Token is invalid"))
            } else {
              setMember(member.get)
              val refreshToken = loginService.generateRefreshToken(memberId())
              response.setHeader(Config.REFRESH_TOKEN_HEADER, refreshToken)
            }
          }
        }
      } else {
        setMember(member.get)
      }
      if (member.isDefined) {
        val _workspaceId = workspaceId()
        if (StringUtils.isEmpty(_workspaceId)) {
          halt(badRequest("X-Workspace-Id is missing"))
        } else if (!loginService.verifyWorkspaceId(memberId(), _workspaceId)) {
          halt(forbidden("Request forbidden"))
        }
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

  def withWorkspace(block: java.lang.String => Any): Any = {
    val _workspaceId = workspaceId()
    if (StringUtils.isEmpty(_workspaceId)) {
      badRequest()
    } else {
      block(_workspaceId)
    }
  }
}
