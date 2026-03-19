package jp.ijufumi.openreports.presentation.controller.private_

import jp.ijufumi.openreports.domain.models.entity.{Lists, Member => MemberModel, Report => ReportModel}
import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, ReportUseCase}
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._
import java.io.File

class ReportServletSpec extends ScalatraFunSuite with MockFactory {
  val loginService = mock[LoginUseCase]
  val reportService = mock[ReportUseCase]
  addServlet(new ReportServlet(loginService, reportService), "/*")

  val member = MemberModel("member-id", None, "test@example.com", "", "Test User", 0, 0)
  val report = ReportModel("report-id", "Test Report", "template-id", Some("datasource-id"), "workspace-id", 1000L, 2000L)

  test("GET / should return list of reports") {
    val reports = Lists(Seq(report), 0, 10, 1)

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.getReports _).expects("workspace-id", 0, 10, "").returns(reports)

    get("/?page=0&limit=10", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("report-id")
      body should include("Test Report")
    }
  }

  test("POST / should create a new report") {
    val requestBody = """{"name":"New Report","templateId":"template-id","dataSourceId":"datasource-id"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.createReport _).expects(where { (workspaceId: String, createReport: Any) =>
      workspaceId == "workspace-id"
    }).returns(Some(report))

    post("/", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(200)
      body should include("report-id")
    }
  }

  test("POST / should return bad request when creation fails") {
    val requestBody = """{"name":"New Report","templateId":"template-id"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.createReport _).expects(*, *).returns(None)

    post("/", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(400)
      body should include("something wrong")
    }
  }

  test("GET /:id should return a specific report") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.getReport _).expects("workspace-id", "report-id").returns(Some(report))

    get("/report-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("report-id")
      body should include("Test Report")
    }
  }

  test("GET /:id should return 404 when report not found") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.getReport _).expects("workspace-id", "nonexistent-id").returns(None)

    get("/nonexistent-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(404)
      body should include("reports not found")
    }
  }

  test("GET /outputs/:id should output report") {
    val file = java.nio.file.Path.of(getClass.getClassLoader.getResource("test.xlsx").toURI).toFile

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.outputReport _).expects("workspace-id", "report-id", false).returns(Some(file))

    get("/outputs/report-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
    }
  }

  test("GET /outputs/:id/pdf should output report as PDF") {
    val file = java.nio.file.Path.of(getClass.getClassLoader.getResource("test.pdf").toURI).toFile

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.outputReport _).expects("workspace-id", "report-id", true).returns(Some(file))

    get("/outputs/report-id/pdf", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
    }
  }

  test("PUT /:id should update a report") {
    val requestBody = """{"name":"Updated Report","templateId":"template-id"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.updateReport _).expects(where { (workspaceId: String, id: String, updateReport: Any) =>
      workspaceId == "workspace-id" && id == "report-id"
    }).returns(Some(report))

    put("/report-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(200)
      body should include("report-id")
    }
  }

  test("DELETE /:id should delete a report") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.deleteReport _).expects("workspace-id", "report-id").returns(())

    delete("/report-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
    }
  }

  // Error handling tests
  test("GET / should return 401 when Authorization header is missing") {
    (loginService.verifyAuthorizationHeader _).expects(argThat { (s: String) =>
      s == null
    }).returns(None)
    get("/?page=0&limit=10", headers = Map("X-Workspace-Id" -> "workspace-id")) {
      status should equal(401)
    }
  }

  test("GET / should return 401 when authorization token is invalid") {
    (loginService.verifyAuthorizationHeader _).expects("invalid-token").returns(None)

    get("/?page=0&limit=10", headers = Map("Authorization" -> "invalid-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(401)
    }
  }

  test("GET / should return 400 when X-Workspace-Id header is missing") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))

    get("/?page=0&limit=10", headers = Map("Authorization" -> "api-token")) {
      status should equal(400)
    }
  }

  test("GET / should return 403 when user doesn't have access to workspace") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "unauthorized-workspace").returns(false)

    get("/?page=0&limit=10", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "unauthorized-workspace")) {
      status should equal(403)
    }
  }

  test("POST / should return 400 when request body is malformed JSON") {
    val malformedJson = """{"name":"New Report","templateId":"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)

    post("/", body = malformedJson.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(400)
    }
  }

  test("POST / should return 400 when required field 'name' is missing") {
    val requestWithoutName = """{"templateId":"template-id"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)

    post("/", body = requestWithoutName.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(400)
    }
  }

  test("PUT /:id should return 404 when updating non-existent report") {
    val requestBody = """{"name":"Updated Report","templateId":"template-id"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.updateReport _).expects(*, *, *).returns(None)

    put("/nonexistent-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(404)
    }
  }

  test("GET /outputs/:id should return 404 when report output fails") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.outputReport _).expects("workspace-id", "nonexistent-id", false).returns(None)

    get("/outputs/nonexistent-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(404)
    }
  }

  test("DELETE /:id should handle deletion with authorization checks") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(false)

    delete("/report-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(403)
    }
  }
}
