package jp.ijufumi.openreports.apis.public_

import jp.ijufumi.openreports.apis.base.APIServletBase
import org.scalatra._

class HealthServlet extends APIServletBase {
  get("/") {
    ok("hello")
  }
}
