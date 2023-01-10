package jp.ijufumi.openreports.apis.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.APIServletBase
import jp.ijufumi.openreports.services.PermissionService

class PermissionServlet @Inject() (permissionService: PermissionService) extends APIServletBase {
  get("/") {
    val result = permissionService.getPermissions
    ok(result)
  }
}
