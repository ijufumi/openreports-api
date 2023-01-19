package jp.ijufumi.openreports.apis.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.apis.base.PrivateAPIServletBase
import jp.ijufumi.openreports.models.inputs.{CreateWorkspace, UpdateWorkspace}
import jp.ijufumi.openreports.services.{LoginService, WorkspaceService}

class WorkspaceServlet @Inject() (
    loginService: LoginService,
    workspaceService: WorkspaceService,
) extends PrivateAPIServletBase(loginService) {
  get("/") {
    val result = workspaceService.getWorkspaces()
    ok(result)
  }

  get("/:id") {
    val _workspaceId = params("id")
    val result = workspaceService.getWorkspace(_workspaceId)
    if (result.isEmpty) {
      notFound("workspace not found")
    } else {
      ok(result.get)
    }
  }

  post("/") {
    val requestVal = extractBody[CreateWorkspace]()
    val _memberId = memberId()
    val result = workspaceService.createAndRelevant(requestVal, _memberId)
    if (result.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(result.get)
    }
  }

  put("/:id") {
    val _workspaceId = params("id")
    val requestVal = extractBody[UpdateWorkspace]()
    val result = workspaceService.updateWorkspace(_workspaceId, requestVal)
    if (result.isEmpty) {
      badRequest("something wrong...")
    } else {
      ok(result.get)
    }
  }
}
