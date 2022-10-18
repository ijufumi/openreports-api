package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.LoginService

class ReportTemplatesServlet @Inject() (loginService: LoginService)
    extends PrivateAPIServletBase(loginService) {
  get("/") {}
}
