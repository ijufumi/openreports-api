package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, WorkspaceService}

class WorkspaceMembersServlet @Inject()(
    loginService: LoginService,
    workspaceService: WorkspaceService,
) extends PrivateAPIServletBase(loginService) {
  get("/") {

  }

  post("/") {

  }

  get("/:id") {

  }

  put("/:id") {

  }

  delete("/:id") {

  }
}
