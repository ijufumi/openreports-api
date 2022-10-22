package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import jp.ijufumi.openreports.vo.request.UpdateReport
import org.scalatra.{NotFound, Ok}

class ReportsServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {

  get("/") {
    val workspaceId = getWorkspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    hookResult(Ok(reportService.getReports(workspaceId, page, limit)))
  }

  post("/") {}

  get("/:id") {
    val workspaceId = getWorkspaceId()
    val id = params("id")
    val report = reportService.getReport(workspaceId, id)
    if (report.isEmpty) {
      hookResult(NotFound("report not found"))
    } else {
      hookResult(Ok(report.get))
    }
  }

  get("/output/:id") {
    val workspaceId = getWorkspaceId()
    val id = params("id")
    val file = reportService.outputReport(workspaceId, id)
    if (file.isEmpty) {
      hookResult(NotFound("report not found"))
    } else {
      hookResult(Ok(file.get))
    }
  }

  put("/:id") {
    val id = params("id")
    val requestParam = extractBody[UpdateReport]()

  }

  delete("/:id") {}
}
