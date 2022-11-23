package jp.ijufumi.openreports.api.public_

import jp.ijufumi.openreports.api.base.APIServletBase
import org.scalatra._

class HealthServlet extends APIServletBase {
  get("/") {
    ok("hello")
  }
}
