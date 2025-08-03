package jp.ijufumi.openreports.presentation.controllers.public_

import jp.ijufumi.openreports.presentation.models.requests.Login
import jp.ijufumi.openreports.presentation.models.responses.Member
import jp.ijufumi.openreports.services.LoginService
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._
import org.json4s.jackson.Serialization.write
import org.json4s.DefaultFormats

class LoginServletSpec extends ScalatraFunSuite with MockFactory {
  implicit val formats = DefaultFormats
  val loginService = mock[LoginService]
  addServlet(new LoginServlet(loginService), "/*")

  test("password login") {
    val request = Login("test@test.com", "password")
    val member = Member("id", "name", "email@email.com", Seq.empty)
    (loginService.login _).expects(request).returns(Some(member))
    (loginService.generateRefreshToken _).expects(member.id).returns("refresh_token")
    (loginService.generateAccessToken _).expects("refresh_token").returns(Some("access_token"))
    post("/password", write(request)) {
      status should equal(200)
    }
  }
}
