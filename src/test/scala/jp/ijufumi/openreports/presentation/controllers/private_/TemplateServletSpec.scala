package jp.ijufumi.openreports.presentation.controllers.private_

import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.presentation.models.responses.{Lists, Member, ReportTemplate}
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import org.scalamock.scalatest.MockFactory
import org.scalatra.test.scalatest._

class TemplateServletSpec extends ScalatraFunSuite with MockFactory {
  val loginService = mock[LoginService]
  val reportService = mock[ReportService]
  addServlet(new TemplateServlet(loginService, reportService), "/*")

  val member = Member("member-id", "test@example.com", "Test User", Seq.empty)
  val template = ReportTemplate("template-id", "Test Template", "/path/to/file", StorageTypes.Local, 1024L, 1000L, 2000L)

  test("GET / should return list of templates") {
    val templates = Lists(Seq(template), 0, 10, 1)

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.getTemplates _).expects("workspace-id", 0, 10).returns(templates)

    get("/?page=0&limit=10", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
      body should include("template-id")
      body should include("Test Template")
    }
  }

  test("PUT /:id should update a template") {
    val requestBody = """{"name":"Updated Template"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.updateTemplate _).expects(where { (workspaceId: String, id: String, updateTemplate: Any) =>
      workspaceId == "workspace-id" && id == "template-id"
    }).returns(Some(template))

    put("/template-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(200)
      body should include("template-id")
    }
  }

  test("PUT /:id should return bad request when update fails") {
    val requestBody = """{"name":"Updated Template"}"""

    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.updateTemplate _).expects(*, *, *).returns(None)

    put("/template-id", body = requestBody.getBytes, headers = Map(
      "Authorization" -> "api-token",
      "X-Workspace-Id" -> "workspace-id",
      "Content-Type" -> "application/json"
    )) {
      status should equal(400)
      body should include("something wrong")
    }
  }

  test("DELETE /:id should delete a template") {
    (loginService.verifyAuthorizationHeader _).expects("api-token").returns(Some(member))
    (loginService.verifyWorkspaceId _).expects(member.id, "workspace-id").returns(true)
    (reportService.deleteTemplate _).expects("workspace-id", "template-id").returns(())

    delete("/template-id", headers = Map("Authorization" -> "api-token", "X-Workspace-Id" -> "workspace-id")) {
      status should equal(200)
    }
  }
}
