package jp.ijufumi.openreports.presentation.controller.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controller.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.request.{CreateReport, UpdateReport}
import jp.ijufumi.openreports.presentation.converter.ReportConverter.conversions._
import jp.ijufumi.openreports.presentation.converter.{ReportConverter => RC}
import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, ReportUseCase}

class ReportServlet @Inject() (loginService: LoginUseCase, reportService: ReportUseCase)
    extends PrivateAPIServletBase(loginService) {

  get("/") {
    withWorkspace{_workspaceId => {
      val page = params("page", "0").toInt
      val limit = params("limit", "10").toInt
      val templateId = params("templateId", "")
      ok(reportService.getReports(_workspaceId, page, limit, templateId))
    }}
  }

  post("/") {
    withWorkspace{_workspaceId => {
      validateBody[CreateReport]{ validatedRequest =>
        val report =
          reportService.createReport(_workspaceId, RC.toCreateReportInput(validatedRequest))
        if (report.isEmpty) {
          badRequest("something wrong...")
        } else {
          ok(report.get)
        }
      }
    }}
  }

  get("/:id") {
    withWorkspace { _workspaceId =>
      val id = params("id")
      val report = reportService.getReport(_workspaceId, id)
      if (report.isEmpty) {
        notFound("reports not found")
      } else {
        ok(report.get)
      }
    }
  }

  get("/outputs/:id") {
    withWorkspace { _workspaceId =>
      val id = params("id")
      val file = reportService.outputReport(_workspaceId, id, asPDF = false)
      if (file.isEmpty) {
        notFound("reports not found")
      } else {
        ok(file.get)
      }
    }
  }

  get("/outputs/:id/pdf") {
    withWorkspace { _workspaceId =>
      val id = params("id")
      val file = reportService.outputReport(_workspaceId, id, asPDF = true)
      if (file.isEmpty) {
        notFound("reports not found")
      } else {
        ok(file.get)
      }
    }
  }

  put("/:id") {
    withWorkspace { _workspaceId =>
      validateBody[UpdateReport] { requestParam =>
        val id = params("id")
        val report =
          reportService.updateReport(_workspaceId, id, RC.toUpdateReportInput(requestParam))
        if (report.isEmpty) {
          notFound("something wrong...")
        } else {
          ok(report.get)
        }
      }
    }
  }

  delete("/:id") {
    withWorkspace { _workspaceId =>
      val id = params("id")
      reportService.deleteReport(_workspaceId, id)
      ok()
    }
  }
}
