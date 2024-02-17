package jp.ijufumi.openreports.interfaces.controllers.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.interfaces.controllers.base.APIServletBase
import jp.ijufumi.openreports.services.RoleService

class RoleServlet @Inject()(roleService: RoleService) extends APIServletBase {
  get("/") {
    val result = roleService.getRoles
    ok(result)
  }
}
