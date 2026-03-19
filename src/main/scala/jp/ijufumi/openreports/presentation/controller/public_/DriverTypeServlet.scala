package jp.ijufumi.openreports.presentation.controller.public_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controller.base.APIServletBase
import jp.ijufumi.openreports.presentation.converter.DriverTypeConverter.conversions._
import jp.ijufumi.openreports.usecase.port.input.DriverTypeUseCase

class DriverTypeServlet @Inject() (driverTypeService: DriverTypeUseCase) extends APIServletBase {
  get("/") {
    val result = driverTypeService.getAll
    ok(result)
  }
}
