package jp.ijufumi.openreports.presentation.controllers.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controllers.base.APIServletBase
import jp.ijufumi.openreports.services.DriverTypeService

class DriverTypeServlet @Inject() (driverTypeService: DriverTypeService) extends APIServletBase {
  get("/") {
    val result = driverTypeService.getAll
    ok(result)
  }
}
