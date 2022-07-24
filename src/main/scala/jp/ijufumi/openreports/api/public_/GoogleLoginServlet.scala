package jp.ijufumi.openreports.api.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.APIServletBase
import jp.ijufumi.openreports.repositories.system.GoogleRepository
import jp.ijufumi.openreports.vo.request.LoginRequest
import org.scalatra._

class GoogleLoginServlet @Inject() (googleLoginService: GoogleRepository) extends APIServletBase {
  get("/google/authorization_url") {
    googleLoginService.getAuthorizationUrl()
  }

  get("/google/login") {
    val state = params.getOrElse("state", halt(400))
    val code = params.getOrElse("code", halt(400))

  }
}
