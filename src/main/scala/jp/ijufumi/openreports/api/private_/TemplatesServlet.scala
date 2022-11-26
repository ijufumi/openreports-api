package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import jp.ijufumi.openreports.vo.request.CreateTemplate

class TemplatesServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {
  get("/") {
    val _workspaceId = workspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    ok(reportService.getTemplates(_workspaceId, page, limit))
  }

  post("/") {
    val _workspaceId = workspaceId()
    val file = fileParams("file")
    val req = extractBody[CreateTemplate]()
    val res = reportService.createTemplate(_workspaceId, req, file)
    if (res.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(res.get)
    }
  }
}
