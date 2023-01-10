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

  get("/:memberId") {
    val _workspaceId = workspaceId()
    val memberId = params("memberId")
    val result = workspaceService.getWorkspaceMember(_workspaceId, memberId)
    if (result.isEmpty) {
      notFound("member not found")
    } else {
      ok(result)
    }
  }

  put("/:memberId") {

  }

  delete("/:memberId") {

  }
}
