package jp.ijufumi.openreports.presentation.controller.private_

import jp.ijufumi.openreports.domain.models.value.enums.ActionTypes
import jp.ijufumi.openreports.domain.models.entity.{
  Function => FunctionModel,
  Member => MemberModel,
  Permission => PermissionModel,
  Workspace => WorkspaceModel,
}
import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, MemberUseCase}
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._

class MemberServletSpec extends ScalatraFunSuite with MockFactory {
  val loginService = mock[LoginUseCase]
  val memberService = mock[MemberUseCase]
  addServlet(new MemberServlet(loginService, memberService), "/*")

  test("GET /status should return member information") {
    val member = MemberModel("member-id", None, "test@example.com", "", "Test User", 0, 0)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)

    get(
      "/status",
      headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id"),
    ) {
      status should equal(200)
      body should include("member-id")
      body should include("test@example.com")
      body should include("Test User")
    }
  }

  test("GET /permissions should return member permissions") {
    val member = MemberModel("member-id", None, "test@example.com", "", "Test User", 0, 0)
    val workspace = WorkspaceModel("workspace-id", "Test Workspace", "test-workspace", 0, 0)
    val function = FunctionModel("function-id", "test", ActionTypes.Reference, 0, 0)
    val permission = Some(PermissionModel(Seq(workspace), Seq(function)))

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (memberService.permissions _).expects(member.id, "workspace-id").returns(permission)

    get(
      "/permissions",
      headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id"),
    ) {
      status should equal(200)
      body should include("workspace-id")
      body should include("function-id")
    }
  }

  test("GET /logout should call logout service") {
    val member = MemberModel("member-id", None, "test@example.com", "", "Test User", 0, 0)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (loginService.logout _).expects("api-token").returns(())

    get(
      "/logout",
      headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id"),
    ) {
      status should equal(200)
    }
  }

  test("PUT /update should update member information") {
    val member = MemberModel("member-id", None, "test@example.com", "", "Test User", 0, 0)
    val updatedMember =
      Some(MemberModel("member-id", None, "test@example.com", "", "Updated Name", 0, 0))
    val requestBody = """{"name":"Updated Name","password":"newpassword"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (memberService.update _)
      .expects(member.id, "Updated Name", "newpassword")
      .returns(updatedMember)

    put(
      "/update",
      body = requestBody.getBytes,
      headers = Map(
        "Authorization" -> "api-token",
        "X-Workspace-Id" -> "workspace-id",
        "Content-Type" -> "application/json",
      ),
    ) {
      status should equal(200)
      body should include("Updated Name")
    }
  }

  test("GET /status missing workspace id header") {
    val member = MemberModel("member-id", None, "test@example.com", "", "Test User", 0, 0)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))

    get("/status", headers = Map("Authorization" -> "api-token")) {
      status should equal(400)
    }
  }

  test("GET /permissions missing workspace id header") {
    val member = MemberModel("member-id", None, "test@example.com", "", "Test User", 0, 0)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))

    get("/permissions", headers = Map("Authorization" -> "api-token")) {
      status should equal(400)
    }
  }

  test("GET /logout missing workspace id header") {
    val member = MemberModel("member-id", None, "test@example.com", "", "Test User", 0, 0)
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))

    get("/logout", headers = Map("Authorization" -> "api-token")) {
      status should equal(400)
    }
  }

  test("PUT /update missing workspace id header") {
    val member = MemberModel("member-id", None, "test@example.com", "", "Test User", 0, 0)
    val requestBody = """{"name":"Updated Name","password":"newpassword"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))

    put(
      "/update",
      body = requestBody.getBytes,
      headers = Map(
        "Authorization" -> "api-token",
        "Content-Type" -> "application/json",
      ),
    ) {
      status should equal(400)
    }
  }
}
