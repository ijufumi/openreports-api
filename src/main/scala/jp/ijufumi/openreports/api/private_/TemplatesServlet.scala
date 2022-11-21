package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import org.scalatra.Ok

class TemplatesServlet @Inject()(loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {
  get("/") {
    val workspaceId = getWorkspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    hookResult(Ok(reportService.getTemplates(workspaceId, page, limit)))
  }

  post("/") {
    val workspaceId = getWorkspaceId()
    val file = fileMultiParams("file")
    file.head
  }
}
