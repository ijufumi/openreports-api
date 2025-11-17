package jp.ijufumi.openreports.presentation.controllers.public_

import jp.ijufumi.openreports.usecase.port.input.RoleUseCase
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._

class RoleServletSpec extends ScalatraFunSuite with MockFactory {
  val roleService = mock[RoleUseCase]
  addServlet(new RoleServlet(roleService), "/*")

  test("simple get") {
    (roleService.getRoles _).expects().returns(Seq.empty)
    get("/") {
      status should equal(200)
      body should equal("[]")
    }
  }
}
