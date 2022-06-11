package jp.ijufumi.openreports.app

import org.scalatra._

class APIServlet extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

}
