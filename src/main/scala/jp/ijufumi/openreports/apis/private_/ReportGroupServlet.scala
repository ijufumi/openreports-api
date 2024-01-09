package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.models.inputs.{CreateReportGroup, CreateTemplate, UpdateReport, UpdateReportGroup, UpdateTemplate}
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
    ok(reportService.createGroup(_workspaceId, requestParam))
  }

  put("/:id") {
    val id = params("id")
    val _workspaceId = workspaceId()
    val requestParam = extractBody[UpdateReportGroup]()
    val report =
      reportService.updateGroup(_workspaceId, id, requestParam)
    if (report.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(report.get)
    }
  }

}
