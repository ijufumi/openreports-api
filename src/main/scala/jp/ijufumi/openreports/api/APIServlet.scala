package jp.ijufumi.openreports.api

import org.scalatra._

class APIServlet extends ScalatraServlet {

  get("/") {
    Ok("hello")
  }
}
