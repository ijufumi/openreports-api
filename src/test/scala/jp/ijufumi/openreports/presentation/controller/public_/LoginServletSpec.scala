package jp.ijufumi.openreports.presentation.controller.public_

import jp.ijufumi.openreports.domain.models.entity.{Member => MemberModel}
import jp.ijufumi.openreports.usecase.port.input.LoginUseCase
import jp.ijufumi.openreports.usecase.port.input.param.LoginInput
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._
import org.json4s.jackson.Serialization.write
import org.json4s.DefaultFormats

class LoginServletSpec extends ScalatraFunSuite with MockFactory {
  implicit val formats: org.json4s.Formats = DefaultFormats
  val loginService = mock[LoginUseCase]
  addServlet(new LoginServlet(loginService), "/*")

  test("password login") {
    val input = LoginInput("test@test.com", "password")
    val member = MemberModel("id", None, "email@email.com", "", "name", 0, 0)
    (loginService.login _).expects(input).returns(Some(member))
    (loginService.generateRefreshToken _).expects(member.id).returns("refresh_token")
    (loginService.generateAccessToken _).expects("refresh_token").returns(Some("access_token"))
    post("/password", write(input)) {
      status should equal(200)
    }
  }
}
