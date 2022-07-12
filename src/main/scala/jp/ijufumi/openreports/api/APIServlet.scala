package jp.ijufumi.openreports.api

import org.scalatra._

class APIServlet extends APIServletBase {
  get("/") {
    Ok("hello")
  }
}
