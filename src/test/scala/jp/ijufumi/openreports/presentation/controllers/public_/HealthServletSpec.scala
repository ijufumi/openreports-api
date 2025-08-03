package jp.ijufumi.openreports.presentation.controllers.public_

import org.scalatra.test.scalatest._

class HealthServletSpec extends ScalatraFunSuite {

  addServlet(classOf[HealthServlet], "/*")

  test("simple get") {
    get("/") {
      status should equal(200)
      body should equal("hello")
    }
  }
}
