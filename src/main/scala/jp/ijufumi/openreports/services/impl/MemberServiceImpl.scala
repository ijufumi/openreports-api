package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.{FunctionRepository, MemberRepository, RoleFunctionRepository, WorkspaceMemberRepository, WorkspaceRepository}
import jp.ijufumi.openreports.presentation.models.responses.{Member, Permission}
import jp.ijufumi.openreports.services.MemberService
import jp.ijufumi.openreports.domain.models.entity.Function.conversions._
import jp.ijufumi.openreports.domain.models.entity.Member.conversions._
import jp.ijufumi.openreports.domain.models.entity.Workspace.conversions._
import jp.ijufumi.openreports.utils.Hash
import slick.jdbc.JdbcBackend.Database

class MemberServiceImpl @Inject() (
    db: Database,
    memberRepository: MemberRepository,
    workspaceMemberRepository: WorkspaceMemberRepository,
    roleFunctionRepository: RoleFunctionRepository,
    functionRepository: FunctionRepository,
    workspaceRepository: WorkspaceRepository,
) extends MemberService {
  override def update(memberId: String, name: String, password: String): Option[Member] = {
    val memberOpt = memberRepository.getById(db, memberId)
    if (memberOpt.isEmpty) {
      return None
    }
    var newName = memberOpt.get.name
    if (name.nonEmpty) {
      newName = name
    }
    var newPassword = memberOpt.get.password
    if (password.nonEmpty) {
      newPassword = Hash.hmacSha256(password)
    }
    val newMember = memberOpt.get.copy(name = newName, password = newPassword)
    memberRepository.update(db, newMember)
    val result = memberRepository.getById(db, memberId)
    result
  }

  override def permissions(memberId: String, workspaceId: String): Option[Permission] = {
    val workspaces = workspaceRepository.getsByMemberId(db, memberId)
    val workspaceMemberOpt = workspaceMemberRepository.getById(db, workspaceId, memberId)
    if (workspaceMemberOpt.isEmpty) {
      return None
    }
    val workspaceMember = workspaceMemberOpt.get
    val functionIds =
      roleFunctionRepository.getByRoleId(db, workspaceMember.roleId).map(m => m.functionId)
    val functions = functionRepository.getsByIds(db, functionIds)
    Some(Permission(workspaces, functions))
  }
}
