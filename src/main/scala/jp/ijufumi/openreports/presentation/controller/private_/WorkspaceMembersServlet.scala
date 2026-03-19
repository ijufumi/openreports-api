package jp.ijufumi.openreports.presentation.controller.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controller.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.request.{
  CreateWorkspaceMember,
  UpdateWorkspaceMember,
}
import jp.ijufumi.openreports.presentation.converter.WorkspaceConverter.conversions._
import jp.ijufumi.openreports.presentation.converter.{WorkspaceConverter => WC}
import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, WorkspaceUseCase}

class WorkspaceMembersServlet @Inject() (
    loginService: LoginUseCase,
    workspaceService: WorkspaceUseCase,
) extends PrivateAPIServletBase(loginService) {
  get("/") {
    withWorkspace { _workspaceId =>
      val result = workspaceService.getWorkspaceMembers(_workspaceId)
      ok(result)
    }
  }

  post("/") {
    withWorkspace { _workspaceId =>
      validateBody[CreateWorkspaceMember] { requestVal =>
        val result = workspaceService.createWorkspaceMember(_workspaceId, WC.toCreateMemberInput(requestVal))
        if (result.isEmpty) {
          badRequest("something wrong...")
        } else {
          ok(result)
        }
      }
    }
  }

  get("/:memberId") {
    withWorkspace { _workspaceId =>
      val memberId = params("memberId")
      val result = workspaceService.getWorkspaceMember(_workspaceId, memberId)
      if (result.isEmpty) {
        notFound("member not found")
      } else {
        ok(result)
      }
    }
  }

  put("/:memberId") {
    withWorkspace { _workspaceId =>
      validateBody[UpdateWorkspaceMember] { requestVal =>
        val memberId = params("memberId")
        val result =
          workspaceService.updateWorkspaceMember(_workspaceId, memberId, WC.toUpdateMemberInput(requestVal))
        if (result.isEmpty) {
          badRequest("something wrong...")
        } else {
          ok(result)
        }
      }
    }
  }

  delete("/:memberId") {
    withWorkspace { _workspaceId =>
      val memberId = params("memberId")
      workspaceService.deleteWorkspaceMember(_workspaceId, memberId)
      ok("success")
    }
  }
}
