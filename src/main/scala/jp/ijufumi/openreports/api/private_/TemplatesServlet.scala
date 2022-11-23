package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import jp.ijufumi.openreports.vo.request.CreateTemplate

class TemplatesServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {
  get("/") {
    val workspaceId = getWorkspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    okResult(reportService.getTemplates(workspaceId, page, limit))
  }

  post("/") {
    val workspaceId = getWorkspaceId()
    val file = fileParams("file")
    val req = extractBody[CreateTemplate]()
    val res = reportService.createTemplate(workspaceId, req, file)
    if (res.isEmpty) {
      badRequestResult("something wrong...")
    } else {
      okResult(res.get)
    }
  }
}
