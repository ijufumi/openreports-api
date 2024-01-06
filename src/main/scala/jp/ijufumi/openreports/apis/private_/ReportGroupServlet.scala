package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.models.inputs.{
  CreateReportGroup,
  CreateTemplate,
  UpdateReport,
  UpdateTemplate,
}
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import org.scalatra.forms._

class ReportGroupServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {
  get("/") {
    val _workspaceId = workspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    ok(reportService.getGroups(_workspaceId, page, limit))
  }

  post("/") {
    val _workspaceId = workspaceId()
    val requestParam = extractBody[CreateReportGroup]()
    ok(reportService.createReportGroup(_workspaceId, requestParam))
  }
}
