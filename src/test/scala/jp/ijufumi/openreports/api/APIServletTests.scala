package jp.ijufumi.openreports.api

import org.scalatra.test.scalatest._

class APIServletTests extends ScalatraFunSuite {

  addServlet(classOf[APIServlet], "/*")

  test("GET / on APIServlet should return status 200") {
    get("/") {
      status should equal(200)
    }
  }

  override def header = ???
}
