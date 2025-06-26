
package jp.ijufumi.openreports.services.impl

import jp.ijufumi.openreports.domain.models.entity.{Function, Member, RoleFunction, Workspace, WorkspaceMember}
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories._
import jp.ijufumi.openreports.presentation.models.responses.Permission
import jp.ijufumi.openreports.utils.Hash
import jp.ijufumi.openreports.utils.IDs
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class MemberServiceImplSpec extends AnyFlatSpec with MockitoSugar {

  "update" should "update member" in {
    // mock
    val db = mock[Database]
    val memberRepository = mock[MemberRepository]
    val workspaceMemberRepository = mock[WorkspaceMemberRepository]
    val roleFunctionRepository = mock[RoleFunctionRepository]
    val functionRepository = mock[FunctionRepository]
    val workspaceRepository = mock[WorkspaceRepository]

    val memberService = new MemberServiceImpl(
      db,
      memberRepository,
      workspaceMemberRepository,
      roleFunctionRepository,
      functionRepository,
      workspaceRepository,
    )

    val memberId = "1"
    val name = "test"
    val password = "password"
    val member = Member(
      id = memberId,
      googleId = None,
      email = "test@test.com",
      password = "old_password",
      name = "old_name",
      createdAt = 0,
      updatedAt = 0,
    )
    val newMember = member.copy(name = name, password = Hash.hmacSha256(password))

    when(memberRepository.getById(db, memberId)).thenReturn(Some(member), Some(newMember))
    memberRepository.update(db, newMember)

    // when
    val actual = memberService.update(memberId, name, password)

    // then
    assert(actual.isDefined)
    assert(actual.get.name == name)
  }

  "permissions" should "return permissions" in {
    // mock
    val db = mock[Database]
    val memberRepository = mock[MemberRepository]
    val workspaceMemberRepository = mock[WorkspaceMemberRepository]
    val roleFunctionRepository = mock[RoleFunctionRepository]
    val functionRepository = mock[FunctionRepository]
    val workspaceRepository = mock[WorkspaceRepository]

    val memberService = new MemberServiceImpl(
      db,
      memberRepository,
      workspaceMemberRepository,
      roleFunctionRepository,
      functionRepository,
      workspaceRepository,
    )

    val memberId = "1"
    val workspaceId = "1"
    val roleId = "1"
    val functionId = "1"
    val workspace = Workspace(workspaceId, "test", "", 0, 0)
    val workspaceMember = WorkspaceMember(workspaceId, memberId, roleId, 0, 0)
    val roleFunction = RoleFunction(IDs.ulid(), roleId, functionId)
    val function = Function(functionId, "test", jp.ijufumi.openreports.domain.models.value.enums.ActionTypes.Reference, 0, 0)

    when(workspaceRepository.getsByMemberId(db, memberId)).thenReturn(Seq(workspace))
    when(workspaceMemberRepository.getById(db, workspaceId, memberId))
      .thenReturn(Some(workspaceMember))
    when(roleFunctionRepository.getByRoleId(db, roleId)).thenReturn(Seq(roleFunction))
    when(functionRepository.getsByIds(db, Seq(functionId))).thenReturn(Seq(function))

    // when
    val actual = memberService.permissions(memberId, workspaceId)

    // then
    assert(actual.isDefined)
    assert(actual.get.workspaces.length == 1)
    assert(actual.get.functions.length == 1)
  }
}
