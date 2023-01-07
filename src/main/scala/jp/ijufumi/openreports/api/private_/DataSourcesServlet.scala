package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{DataSourceService, LoginService}

class DataSourcesServlet @Inject() (
    loginService: LoginService,
    dataSourceService: DataSourceService,
) extends PrivateAPIServletBase(loginService) {

  get("/") {
    val _workspaceId = workspaceId()
    val result = dataSourceService.getDataSources(_workspaceId)
    ok(result)
  }

  post("/") {}

  put("/:id") {}

  get("/:id") {}

  delete("/:id") {}
}
