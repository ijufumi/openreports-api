package jp.ijufumi.openreports.presentation.controllers.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controllers.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.models.requests.{
  CreateWorkspaceMember,
  UpdateWorkspaceMember,
}
import jp.ijufumi.openreports.services.{LoginService, WorkspaceService}

class WorkspaceMembersServlet @Inject() (
    loginService: LoginService,
    workspaceService: WorkspaceService,
) extends PrivateAPIServletBase(loginService) {
  get("/") {
    val _workspaceId = workspaceId()
    val result = workspaceService.getWorkspaceMembers(_workspaceId)
    ok(result)
  }

  post("/") {
    val _workspaceId = workspaceId()
    val requestVal = extractBody[CreateWorkspaceMember]()
    val result = workspaceService.createWorkspaceMember(_workspaceId, requestVal)
    if (result.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(result)
    }
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
    val _workspaceId = workspaceId()
    val memberId = params("memberId")
    val requestVal = extractBody[UpdateWorkspaceMember]()
    val result = workspaceService.updateWorkspaceMember(_workspaceId, memberId, requestVal)
    if (result.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(result)
    }
  }

  delete("/:memberId") {
    val _workspaceId = workspaceId()
    val memberId = params("memberId")
    workspaceService.deleteWorkspaceMember(_workspaceId, memberId)
    ok("success")
  }
}
