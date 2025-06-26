
package jp.ijufumi.openreports.services.impl

import jp.ijufumi.openreports.configs.Config
import jp.ijufumi.openreports.domain.models.entity.Member
import jp.ijufumi.openreports.infrastructure.datastores.cache.CacheWrapper
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.{
  MemberRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.infrastructure.google.auth.GoogleRepository
import jp.ijufumi.openreports.presentation.models.requests.Login
import jp.ijufumi.openreports.services.WorkspaceService
import jp.ijufumi.openreports.utils.Hash
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class LoginServiceImplSpec extends AnyFlatSpec with MockitoSugar {

  "login" should "return Member if email and password are correct" in {
    // mock
    val db = mock[Database]
    val memberRepository = mock[MemberRepository]
    val workspaceRepository = mock[WorkspaceRepository]
    val googleRepository = mock[GoogleRepository]
    val workspaceService = mock[WorkspaceService]
    val cacheWrapper = mock[CacheWrapper]

    val loginService =
      new LoginServiceImpl(db, memberRepository, workspaceRepository, googleRepository, workspaceService, cacheWrapper)

    val email = "test@test.com"
    val password = "password"
    val input = Login(email, password)
    val member = Member(
      id = "1",
      googleId = None,
      email = email,
      password = Hash.hmacSha256(password),
      name = "test",
      createdAt = 0,
      updatedAt = 0,
    )

    when(memberRepository.getMemberByEmail(db, email)).thenReturn(Some(member))
    when(workspaceRepository.getsByMemberId(db, member.id)).thenReturn(Seq.empty)

    // when
    val actual = loginService.login(input)

    // then
    assert(actual.isDefined)
    assert(actual.get.email == email)
  }

  it should "return None if email does not exist" in {
    // mock
    val db = mock[Database]
    val memberRepository = mock[MemberRepository]
    val workspaceRepository = mock[WorkspaceRepository]
    val googleRepository = mock[GoogleRepository]
    val workspaceService = mock[WorkspaceService]
    val cacheWrapper = mock[CacheWrapper]

    val loginService =
      new LoginServiceImpl(db, memberRepository, workspaceRepository, googleRepository, workspaceService, cacheWrapper)

    val email = "test@test.com"
    val password = "password"
    val input = Login(email, password)

    when(memberRepository.getMemberByEmail(db, email)).thenReturn(None)

    // when
    val actual = loginService.login(input)

    // then
    assert(actual.isEmpty)
  }

  it should "return None if password does not match" in {
    // mock
    val db = mock[Database]
    val memberRepository = mock[MemberRepository]
    val workspaceRepository = mock[WorkspaceRepository]
    val googleRepository = mock[GoogleRepository]
    val workspaceService = mock[WorkspaceService]
    val cacheWrapper = mock[CacheWrapper]

    val loginService =
      new LoginServiceImpl(db, memberRepository, workspaceRepository, googleRepository, workspaceService, cacheWrapper)

    val email = "test@test.com"
    val password = "password"
    val wrongPassword = "wrong_password"
    val input = Login(email, wrongPassword)
    val member = Member(
      id = "1",
      googleId = None,
      email = email,
      password = Hash.hmacSha256(password),
      name = "test",
      createdAt = 0,
      updatedAt = 0,
    )

    when(memberRepository.getMemberByEmail(db, email)).thenReturn(Some(member))

    // when
    val actual = loginService.login(input)

    // then
    assert(actual.isEmpty)
  }
}
