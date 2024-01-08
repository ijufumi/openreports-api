package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import jp.ijufumi.openreports.models.inputs.{CreateReport, UpdateReport}

class ReportServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {

  get("/") {
    val _workspaceId = workspaceId()
    val page = params("page", "0").toInt
    val limit = params("limit", "10").toInt
    val templateId = params("templateId", "")
    ok(reportService.getReports(_workspaceId, page, limit, templateId))
  }

  post("/") {
    val _workspaceId = workspaceId()
    val requestParam = extractBody[CreateReport]()
    val report =
      reportService.createReport(_workspaceId, requestParam)
    if (report.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(report.get)
    }
  }

  get("/:id") {
    val _workspaceId = workspaceId()
    val id = params("id")
    val report = reportService.getReport(_workspaceId, id)
    if (report.isEmpty) {
      notFound("reports not found")
    } else {
      ok(report.get)
    }
  }

  get("/outputs/:id") {
    val _workspaceId = workspaceId()
    val id = params("id")
    val file = reportService.outputReport(_workspaceId, id)
    if (file.isEmpty) {
      notFound("reports not found")
    } else {
      ok(file.get)
    }
  }

  put("/:id") {
    val id = params("id")
    val _workspaceId = workspaceId()
    val requestParam = extractBody[UpdateReport]()
    val report =
      reportService.updateReport(_workspaceId, id, requestParam)
    if (report.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(report.get)
    }
  }

  delete("/:id") {
    val id = params("id")
    val _workspaceId = workspaceId()
    reportService.deleteReport(_workspaceId, id)
    ok()
  }
}
