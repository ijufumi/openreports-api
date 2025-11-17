package jp.ijufumi.openreports.presentation.controller.public_

import jp.ijufumi.openreports.presentation.controller.base.APIServletBase
import org.scalatra._

class HealthServlet extends APIServletBase {
  get("/") {
    ok("hello")
  }
}
