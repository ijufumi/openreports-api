package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.LoginService

class ReportsServlet @Inject() (loginService: LoginService)
    extends PrivateAPIServletBase(loginService) {

  get("/") {}

  post("/") {}

  put("/:id") {}

  delete("/:id") {}
}
