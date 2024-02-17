package jp.ijufumi.openreports.interfaces.controllers.public_

import jp.ijufumi.openreports.interfaces.controllers.base.APIServletBase
import org.scalatra._

class HealthServlet extends APIServletBase {
  get("/") {
    ok("hello")
  }
}
