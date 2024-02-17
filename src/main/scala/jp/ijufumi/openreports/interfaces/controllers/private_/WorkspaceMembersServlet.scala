package jp.ijufumi.openreports.interfaces.controllers.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.interfaces.controllers.base.PrivateAPIServletBase
import jp.ijufumi.openreports.interfaces.models.inputs.{CreateWorkspaceMember, UpdateWorkspaceMember}
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
    val requestVal = extractBody[CreateWorkspaceMember]()
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
