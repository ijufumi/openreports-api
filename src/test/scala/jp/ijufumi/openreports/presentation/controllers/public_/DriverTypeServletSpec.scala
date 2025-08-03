package jp.ijufumi.openreports.presentation.controllers.public_

import jp.ijufumi.openreports.services.DriverTypeService
import org.scalatra.test.scalatest._
import org.scalamock.scalatest.MockFactory

class DriverTypeServletSpec extends ScalatraFunSuite with MockFactory {
  val driverTypeService = mock[DriverTypeService]
  addServlet(new DriverTypeServlet(driverTypeService), "/*")

  test("simple get") {
    (driverTypeService.getAll _).expects().returns(Seq.empty)
    get("/") {
      status should equal(200)
      body should equal("[]")
    }
  }
}
