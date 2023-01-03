package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import jp.ijufumi.openreports.vo.request.UpdateReport

class ReportsServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {

  get("/") {
    val _workspaceId = workspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    val templateId = params("templateId")
    ok(reportService.getReports(_workspaceId, page, limit, templateId))
  }

  post("/") {}

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
      reportService.updateReport(_workspaceId, id, requestParam.name, requestParam.reportTemplateId)
    if (report.isEmpty) {
      notFound("Failed to update reports")
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
