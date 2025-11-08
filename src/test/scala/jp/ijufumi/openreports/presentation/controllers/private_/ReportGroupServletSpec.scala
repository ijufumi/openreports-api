package jp.ijufumi.openreports.presentation.controllers.private_

import jp.ijufumi.openreports.presentation.models.responses.{Lists, Member, ReportGroup}
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._

class ReportGroupServletSpec extends ScalatraFunSuite with MockFactory {
  val loginService = mock[LoginService]
  val reportService = mock[ReportService]
  addServlet(new ReportGroupServlet(loginService, reportService), "/*")

  val member = Member("member-id", "test@example.com", "Test User", Seq.empty)
  val reportGroup = ReportGroup("group-id", "Test Group", 1000L, 2000L)

  test("GET / should return list of report groups") {
    val groups = Lists(Seq(reportGroup), 0, 10, 1)

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.getGroups _).expects("workspace-id", 0, 10).returns(groups)

    get("/?page=0&limit=10", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("group-id")
      body should include("Test Group")
    }
  }

  test("POST / should create a new report group") {
    val requestBody = """{"name":"New Group"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.createGroup _).expects(where { (workspaceId: String, createGroup: Any) =>
      workspaceId == "workspace-id"
    }).returns(Some(reportGroup))

    post("/", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(200)
      body should include("group-id")
    }
  }

  test("PUT /:id should update a report group") {
    val requestBody = """{"name":"Updated Group"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.updateGroup _).expects(where { (workspaceId: String, id: String, updateGroup: Any) =>
      workspaceId == "workspace-id" && id == "group-id"
    }).returns(Some(reportGroup))

    put("/group-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(200)
      body should include("group-id")
    }
  }

  test("PUT /:id should return bad request when update fails") {
    val requestBody = """{"name":"Updated Group"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.updateGroup _).expects(*, *, *).returns(None)

    put("/group-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(400)
      body should include("something wrong")
    }
  }

  test("DELETE /:id should delete a report group") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.deleteGroup _).expects("workspace-id", "group-id").returns(())

    delete("/group-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
    }
  }
}
