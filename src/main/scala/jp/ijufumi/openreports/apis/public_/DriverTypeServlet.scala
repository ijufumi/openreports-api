package jp.ijufumi.openreports.apis.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.APIServletBase
import jp.ijufumi.openreports.services.DriverTypeService

class DriverTypeServlet @Inject() (driverTypeService: DriverTypeService) extends APIServletBase {
  get("/") {
    val result = driverTypeService.getAll
    ok(result)
  }
}
