package jp.ijufumi.openreports.presentation.controller.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controller.base.APIServletBase
import jp.ijufumi.openreports.presentation.converter.RoleConverter.conversions._
import jp.ijufumi.openreports.usecase.port.input.RoleUseCase

class RoleServlet @Inject()(roleService: RoleUseCase) extends APIServletBase {
  get("/") {
    val result = roleService.getRoles
    ok(result)
  }
}
