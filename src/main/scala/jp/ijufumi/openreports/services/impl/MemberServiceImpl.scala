package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.models.outputs.{
  Member => MemberResponse,
  Permissions,
  Workspace => WorkspaceResponse,
}
import jp.ijufumi.openreports.gateways.datastores.database.repositories.{
  MemberRepository,
  WorkspaceMemberRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.services.MemberService

class MemberServiceImpl @Inject() (
    memberRepository: MemberRepository,
    workspaceRepository: WorkspaceRepository,
) extends MemberService {
  override def update(memberId: String, name: String, password: String): Option[MemberResponse] = {
    val memberOpt = memberRepository.getById(memberId)
    if (memberOpt.isEmpty) {
      return None
    }
    val newMember = memberOpt.get.copy(name = name, password = password)
    memberRepository.update(newMember)
    Some(MemberResponse(memberRepository.getById(memberId).get, Seq.empty, ""))
  }

  override def permissions(memberId: String): Option[Permissions] = {
    val workspaces = workspaceRepository.getsByMemberId(memberId)

    Some(Permissions(workspaces.map(w => WorkspaceResponse(w))))
  }
}
