package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, WorkspaceService}

class WorkspaceMembersServlet @Inject()(
    loginService: LoginService,
    workspaceService: WorkspaceService,
) extends PrivateAPIServletBase(loginService) {
  get("/") {
    val _workspaceId = workspaceId()
    val result = workspaceService.getWorkspaceMembers(_workspaceId)
    ok(result)
  }

  post("/") {

  }

  get("/:id") {
    val _workspaceId = workspaceId()
    val id = params("id")
    val result = workspaceService.getWorkspaceMember(_workspaceId, id)
    if (result.isEmpty) {
      notFound("member not found")
    } else {
      ok(result)
    }
  }

  put("/:id") {

  }

  delete("/:id") {

  }
}
