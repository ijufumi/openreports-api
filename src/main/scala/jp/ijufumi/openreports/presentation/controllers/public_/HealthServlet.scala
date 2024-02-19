package jp.ijufumi.openreports.presentation.controllers.public_

import jp.ijufumi.openreports.presentation.controllers.base.APIServletBase
import org.scalatra._

class HealthServlet extends APIServletBase {
  get("/") {
    ok("hello")
  }
}
