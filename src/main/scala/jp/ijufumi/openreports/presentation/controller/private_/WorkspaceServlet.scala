package jp.ijufumi.openreports.presentation.controller.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controller.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.request.{CreateWorkspace, UpdateWorkspace}
import jp.ijufumi.openreports.presentation.converter.WorkspaceConverter.conversions._
import jp.ijufumi.openreports.presentation.converter.{WorkspaceConverter => WC}
import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, WorkspaceUseCase}

class WorkspaceServlet @Inject() (
    loginService: LoginUseCase,
    workspaceService: WorkspaceUseCase,
) extends PrivateAPIServletBase(loginService) {
  get("/") {
    val _memberId = memberId()
    val result = workspaceService.getWorkspacesByMemberId(_memberId)
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
    validateBody[CreateWorkspace] { requestVal =>
      val _memberId = memberId()
      val result = workspaceService.createAndRelevant(requestVal.name, _memberId)
      if (result.isEmpty) {
        badRequest("something wrong...")
      } else {
        ok(result.get)
      }
    }
  }

  put("/:id") {
    validateBody[UpdateWorkspace] { requestVal =>
      val _workspaceId = params("id")
      val result = workspaceService.updateWorkspace(_workspaceId, WC.toUpdateWorkspaceInput(requestVal))
      if (result.isEmpty) {
        badRequest("something wrong...")
      } else {
        ok(result.get)
      }
    }
  }
}
