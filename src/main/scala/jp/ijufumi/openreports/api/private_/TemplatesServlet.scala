package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import jp.ijufumi.openreports.vo.request.CreateTemplate
import org.scalatra.Ok

class TemplatesServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {
  get("/") {
    val workspaceId = getWorkspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    ok(reportService.getTemplates(workspaceId, page, limit))
  }

  post("/") {
    val workspaceId = getWorkspaceId()
    val file = fileMultiParams("file")
    val request = extractBody[CreateTemplate]()
    val response = reportService.createTemplate(workspaceId, request, file.head)
    if (response.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(response.get)
    }
  }
}
