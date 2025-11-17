package jp.ijufumi.openreports.presentation.controller.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controller.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.request.{CreateDataSource, UpdateDataSource}
import jp.ijufumi.openreports.usecase.port.input.{DataSourceUseCase, LoginUseCase}

class DataSourceServlet @Inject() (
    loginService: LoginUseCase,
    dataSourceService: DataSourceUseCase,
) extends PrivateAPIServletBase(loginService) {

  get("/") {
    val _workspaceId = workspaceId()
    val result = dataSourceService.getDataSources(_workspaceId)
    ok(result)
  }

  post("/") {
    val _workspaceId = workspaceId()
    val requestVal = extractBody[CreateDataSource]()
    val result = dataSourceService.registerDataSource(_workspaceId, requestVal)
    if (result.isEmpty) {
      badRequest("something wrong")
    } else {
      ok(result)
    }
  }

  put("/:id") {
    val _workspaceId = workspaceId()
    val id = params("id")
    val requestVal = extractBody[UpdateDataSource]()
    val result = dataSourceService.updateDataSource(_workspaceId, id, requestVal)
    if (result.isEmpty) {
      badRequest("something wrong")
    } else {
      ok(result)
    }
  }

  get("/:id") {
    val _workspaceId = workspaceId()
    val id = params("id")
    val result = dataSourceService.getDataSource(_workspaceId, id)
    if (result.isEmpty) {
      notFound("doesn't exist")
    } else {
      ok(result)
    }
  }

  delete("/:id") {
    val _workspaceId = workspaceId()
    val id = params("id")
    dataSourceService.deleteDataSource(_workspaceId, id)
    ok("success")
  }
}
