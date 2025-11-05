package jp.ijufumi.openreports.presentation.controllers.private_

import jp.ijufumi.openreports.presentation.models.responses.{Member, Workspace}
import jp.ijufumi.openreports.services.{LoginService, WorkspaceService}
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._

class WorkspaceServletSpec extends ScalatraFunSuite with MockFactory {
  val loginService = mock[LoginService]
  val workspaceService = mock[WorkspaceService]
  addServlet(new WorkspaceServlet(loginService, workspaceService), "/*")

  val member = Member("member-id", "test@example.com", "Test User", Seq.empty)
  val workspace = Workspace("workspace-id", "Test Workspace", "test-workspace")

  test("GET / should return list of workspaces by member ID") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.getWorkspacesByMemberId _).expects(member.id).returns(Seq(workspace))

    get("/", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("workspace-id")
      body should include("Test Workspace")
    }
  }

  test("GET /:id should return a specific workspace") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.getWorkspace _).expects("workspace-id").returns(Some(workspace))

    get("/workspace-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("workspace-id")
      body should include("Test Workspace")
    }
  }

  test("GET /:id should return 404 when workspace not found") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.getWorkspace _).expects("nonexistent-id").returns(None)

    get("/nonexistent-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(404)
      body should include("workspace not found")
    }
  }

  test("POST / should create a new workspace") {
    val requestBody = """{"name":"New Workspace"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.createAndRelevant _).expects(where { (createWorkspace: Any, memberId: String) =>
      memberId == member.id
    }).returns(Some(workspace))

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
    val requestBody = """{"name":"New Workspace"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.createAndRelevant _).expects(*, *).returns(None)

    post("/", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(400)
      body should include("something wrong")
    }
  }

  test("PUT /:id should update a workspace") {
    val requestBody = """{"name":"Updated Workspace"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.updateWorkspace _).expects(where { (workspaceId: String, updateWorkspace: Any) =>
      workspaceId == "workspace-id"
    }).returns(Some(workspace))

    put("/workspace-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(200)
      body should include("workspace-id")
    }
  }

  test("PUT /:id should return bad request when update fails") {
    val requestBody = """{"name":"Updated Workspace"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (workspaceService.updateWorkspace _).expects(*, *).returns(None)

    put("/workspace-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(400)
      body should include("something wrong")
    }
  }
}
