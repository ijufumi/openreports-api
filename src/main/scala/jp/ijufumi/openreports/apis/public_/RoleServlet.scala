package jp.ijufumi.openreports.apis.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.APIServletBase
import jp.ijufumi.openreports.services.RoleService

class RoleServlet @Inject()(roleService: RoleService) extends APIServletBase {
  get("/") {
    val result = roleService.getRoles
    ok(result)
  }
}
