package jp.ijufumi.openreports.presentation.controller.public_

import jp.ijufumi.openreports.usecase.port.input.DriverTypeUseCase
import org.scalatra.test.scalatest._
import org.scalamock.scalatest.MockFactory

class DriverTypeServletSpec extends ScalatraFunSuite with MockFactory {
  val driverTypeService = mock[DriverTypeUseCase]
  addServlet(new DriverTypeServlet(driverTypeService), "/*")

  test("simple get") {
    (() => driverTypeService.getAll).expects().returns(Seq.empty)
    get("/") {
      status should equal(200)
      body should equal("[]")
    }
  }
}
