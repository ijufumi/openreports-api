package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{DataSourceService, LoginService}
import jp.ijufumi.openreports.models.inputs.{CreateDataSource, UpdateDataSource}

class DataSourceServlet @Inject()(
    loginService: LoginService,
    dataSourceService: DataSourceService,
) extends PrivateAPIServletBase(loginService) {

  get("/") {
    val _workspaceId = workspaceId()
    val result = dataSourceService.getDataSources(_workspaceId)
    ok(result)
  }

  post("/") {
    val requestVal = extractBody[CreateDataSource]()
  }

  put("/:id") {
    val requestVal = extractBody[UpdateDataSource]()
  }

  get("/:id") {}

  delete("/:id") {}
}
