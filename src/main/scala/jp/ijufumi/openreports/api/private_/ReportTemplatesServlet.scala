package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import org.scalatra.Ok

class ReportTemplatesServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {
  get("/") {
    val workspaceId = params("workspaceId")
    val page = params("page").toInt
    val limit = params("limit").toInt
    hookResult(Ok(reportService.getReportTemplates(workspaceId, page, limit)))
  }
}
