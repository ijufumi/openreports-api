package jp.ijufumi.openreports.presentation.controllers.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controllers.base.APIServletBase
import jp.ijufumi.openreports.services.RoleService

class RoleServlet @Inject()(roleService: RoleService) extends APIServletBase {
  get("/") {
    val result = roleService.getRoles
    ok(result)
  }
}
