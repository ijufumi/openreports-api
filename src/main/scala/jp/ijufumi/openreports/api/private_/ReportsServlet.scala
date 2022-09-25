package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import org.scalatra.{NotFound, Ok}

class ReportsServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {

  get("/") {
    hookResult(Ok(reportService.getReports()))
  }

  post("/") {}

  get("/:id") {
    val report = reportService.getReport(params("id"))
    if (report.isEmpty) {
      hookResult(NotFound("report not found"))
    } else {
      hookResult(Ok(report.get))
    }
  }

  get("/output/:id") {
    val file = reportService.outputReport(params("id"))
    if (file.isEmpty) {
      hookResult(NotFound("report not found"))
    } else {
      hookResult(Ok(file.get))
    }
  }

  put("/:id") {}

  delete("/:id") {}
}