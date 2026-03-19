package jp.ijufumi.openreports.presentation.controller.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.presentation.controller.base.PrivateAPIServletBase
import jp.ijufumi.openreports.presentation.request.{UpdateMember, UpdateReport}
import jp.ijufumi.openreports.presentation.converter.MemberConverter.conversions._
import jp.ijufumi.openreports.usecase.port.input.{LoginUseCase, MemberUseCase}

class MemberServlet @Inject() (loginService: LoginUseCase, memberService: MemberUseCase)
    extends PrivateAPIServletBase(loginService) {
  get("/status") {
    ok(member())
  }

  get("/permissions") {
    withWorkspace { _workspaceId =>
      val _memberId = memberId()
      ok(memberService.permissions(_memberId, _workspaceId))
    }
  }

  get("/logout") {
    val header = authorizationHeader()
    loginService.logout(header)
  }

  put("/update") {
    validateBody[UpdateMember] { requestParam =>
      val _member = member()
      ok(memberService.update(_member.id, requestParam.name, requestParam.password))
    }
  }

  post("/access-token") {

  }
}
