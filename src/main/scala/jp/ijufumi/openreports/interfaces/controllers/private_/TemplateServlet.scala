package jp.ijufumi.openreports.interfaces.controllers.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.interfaces.controllers.base.PrivateAPIServletBase
import jp.ijufumi.openreports.interfaces.models.inputs.{CreateTemplate, UpdateReport, UpdateTemplate}
import org.scalatra.forms._
import jp.ijufumi.openreports.services.{LoginService, ReportService}

class TemplateServlet @Inject()(loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {
  get("/") {
    val _workspaceId = workspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    ok(reportService.getTemplates(_workspaceId, page, limit))
  }

  post("/") {
    val postForm = mapping(
      "name" -> text(),
    )(CreateTemplate.apply)

    validate(postForm)(
      (errors) => {
        badRequest(errors)
      },
      (form: CreateTemplate) => {
        val _workspaceId = workspaceId()
        val file = fileParams("file")
        val res = reportService.createTemplate(_workspaceId, form, file)
        if (res.isEmpty) {
          badRequest("something wrong...")
        } else {
          ok(res.get)
        }
      },
    )
  }

  put("/:id") {
    val id = params("id")
    val requestParam = extractBody[UpdateTemplate]()
    val _workspaceId = workspaceId()
    val res = reportService.updateTemplate(_workspaceId, id, requestParam)
    if (res.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(res.get)
    }
  }

  delete("/:id") {
    val id = params("id")
    val _workspaceId = workspaceId()
    reportService.deleteTemplate(_workspaceId, id)
    ok()
  }
}
