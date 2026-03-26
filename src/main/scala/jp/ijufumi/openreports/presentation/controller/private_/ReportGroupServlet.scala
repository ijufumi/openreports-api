package jp.ijufumi.openreports.presentation.controller.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controller.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.request.{
  CreateReportGroup,
  CreateTemplate,
  UpdateReport,
  UpdateReportGroup,
  UpdateTemplate,
}
import jp.ijufumi.openreports.presentation.converter.ReportConverter.conversions._
import jp.ijufumi.openreports.presentation.converter.{ReportConverter => RC}
import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, ReportUseCase}
import org.scalatra.forms._

class ReportGroupServlet @Inject() (loginService: LoginUseCase, reportService: ReportUseCase)
    extends PrivateAPIServletBase(loginService) {
  get("/") {
    withWorkspace { _workspaceId =>
      val page = params("page").toInt
      val limit = params("limit").toInt
      ok(reportService.getGroups(_workspaceId, page, limit))
    }
  }

  post("/") {
    withWorkspace { _workspaceId =>
      validateBody[CreateReportGroup] { requestParam =>
        ok(reportService.createGroup(_workspaceId, RC.toCreateGroupInput(requestParam)))
      }
    }
  }

  put("/:id") {
    withWorkspace { _workspaceId =>
      validateBody[UpdateReportGroup] { requestParam =>
        val id = params("id")
        val report =
          reportService.updateGroup(_workspaceId, id, RC.toUpdateGroupInput(requestParam))
        if (report.isEmpty) {
          badRequest("something wrong...")
        } else {
          ok(report.get)
        }
      }
    }
  }

  delete("/:id") {
    withWorkspace { _workspaceId =>
      val id = params("id")
      ok(reportService.deleteGroup(_workspaceId, id))
    }
  }
}
