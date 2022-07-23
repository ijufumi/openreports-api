package jp.ijufumi.openreports.api

import jp.ijufumi.openreports.api.public_.HealthServlet
import org.scalatra.test.scalatest._

class APIServletTests extends ScalatraFunSuite {

  addServlet(classOf[HealthServlet], "/*")

  test("GET / on APIServlet should return status 200") {
    get("/") {
      status should equal(200)
    }
  }

  override def header = ???
}
