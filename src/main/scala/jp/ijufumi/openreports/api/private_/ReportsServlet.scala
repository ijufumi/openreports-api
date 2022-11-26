package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import jp.ijufumi.openreports.vo.request.UpdateReport

class ReportsServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {

  get("/") {
    val workspaceId = workspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    ok(reportService.getReports(workspaceId, page, limit))
  }

  post("/") {}

  get("/:id") {
    val workspaceId = workspaceId()
    val id = params("id")
    val report = reportService.getReport(workspaceId, id)
    if (report.isEmpty) {
      notFound("reports not found")
    } else {
      ok(report.get)
    }
  }

  get("/outputs/:id") {
    val workspaceId = workspaceId()
    val id = params("id")
    val file = reportService.outputReport(workspaceId, id)
    if (file.isEmpty) {
      notFound("reports not found")
    } else {
      ok(file.get)
    }
  }

  put("/:id") {
    val id = params("id")
    val workspaceId = workspaceId()
    val requestParam = extractBody[UpdateReport]()
    val report =
      reportService.updateReport(workspaceId, id, requestParam.name, requestParam.reportTemplateId)
    if (report.isEmpty) {
      notFound("Failed to update reports")
    } else {
      ok(report.get)
    }
  }

  delete("/:id") {
    val id = params("id")
    val workspaceId = workspaceId()
    reportService.deleteReport(workspaceId, id)
    ok()
  }
}
