package jp.ijufumi.openreports.presentation.controllers.private_

import jp.ijufumi.openreports.presentation.models.responses.{Member, WorkspaceMember}
import jp.ijufumi.openreports.services.{LoginService, WorkspaceService}
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._

class WorkspaceMembersServletSpec extends ScalatraFunSuite with MockFactory {
  val loginService = mock[LoginService]
  val workspaceService = mock[WorkspaceService]
  addServlet(new WorkspaceMembersServlet(loginService, workspaceService), "/*")

  val member = Member("member-id", "test@example.com", "Test User", Seq.empty)
  val workspaceMember = WorkspaceMember("workspace-id", "member-id", "role-id", None)

  test("GET / should return list of workspace members") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.getWorkspaceMembers _).expects("workspace-id").returns(Seq(workspaceMember))

    get("/", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("workspace-id")
      body should include("member-id")
    }
  }

  test("POST / should create a new workspace member") {
    val requestBody = """{"email":"newmember@example.com","roleId":"role-id"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.createWorkspaceMember _).expects(where { (workspaceId: String, createMember: Any) =>
      workspaceId == "workspace-id"
    }).returns(Some(workspaceMember))

    post("/", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(200)
      body should include("workspace-id")
    }
  }

  test("POST / should return bad request when creation fails") {
    val requestBody = """{"email":"newmember@example.com","roleId":"role-id"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.createWorkspaceMember _).expects(*, *).returns(None)

    post("/", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(400)
      body should include("something wrong")
    }
  }

  test("GET /:memberId should return a specific workspace member") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.getWorkspaceMember _).expects("workspace-id", "member-id").returns(Some(workspaceMember))

    get("/member-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("member-id")
    }
  }

  test("GET /:memberId should return 404 when member not found") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.getWorkspaceMember _).expects("workspace-id", "nonexistent-id").returns(None)

    get("/nonexistent-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(404)
      body should include("member not found")
    }
  }

  test("PUT /:memberId should update a workspace member") {
    val requestBody = """{"roleId":"new-role-id"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.updateWorkspaceMember _).expects(where { (workspaceId: String, memberId: String, updateMember: Any) =>
      workspaceId == "workspace-id" && memberId == "member-id"
    }).returns(Some(workspaceMember))

    put("/member-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(200)
      body should include("member-id")
    }
  }

  test("PUT /:memberId should return bad request when update fails") {
    val requestBody = """{"roleId":"new-role-id"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.updateWorkspaceMember _).expects(*, *, *).returns(None)

    put("/member-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(400)
      body should include("something wrong")
    }
  }

  test("DELETE /:memberId should delete a workspace member") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.deleteWorkspaceMember _).expects("workspace-id", "member-id").returns(())

    delete("/member-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should equal("success")
    }
  }
}
