package jp.ijufumi.openreports.presentation.controllers.private_

import jp.ijufumi.openreports.domain.models.value.enums.ActionTypes
import jp.ijufumi.openreports.presentation.models.responses.{Function, Member, Permission, Workspace}
import jp.ijufumi.openreports.services.{LoginService, MemberService}
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._

class MemberServletSpec extends ScalatraFunSuite with MockFactory {
  val loginService = mock[LoginService]
  val memberService = mock[MemberService]
  addServlet(new MemberServlet(loginService, memberService), "/*")

  test("GET /status should return member information") {
    val member = Member("member-id", "test@example.com", "Test User", Seq.empty)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)

    get("/status", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("member-id")
      body should include("test@example.com")
      body should include("Test User")
    }
  }

  test("GET /permissions should return member permissions") {
    val member = Member("member-id", "test@example.com", "Test User", Seq.empty)
    val workspace = Workspace("workspace-id", "Test Workspace", "test-workspace")
    val function = Function("function-id", ActionTypes.Reference)
    val permission = Some(Permission(Seq(workspace), Seq(function)))

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (memberService.permissions _).expects(member.id, "workspace-id").returns(permission)

    get("/permissions", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("workspace-id")
      body should include("function-id")
    }
  }

  test("GET /logout should call logout service") {
    val member = Member("member-id", "test@example.com", "Test User", Seq.empty)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (loginService.logout _).expects("api-token").returns(())

    get("/logout", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
    }
  }

  test("PUT /update should update member information") {
    val member = Member("member-id", "test@example.com", "Test User", Seq.empty)
    val updatedMember = Some(Member("member-id", "test@example.com", "Updated Name", Seq.empty))
    val requestBody = """{"name":"Updated Name","password":"newpassword"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (memberService.update _).expects(member.id, "Updated Name", "newpassword").returns(updatedMember)

    put("/update", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(200)
      body should include("Updated Name")
    }
  }
}
