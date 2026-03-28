package jp.ijufumi.openreports.usecase.interactor

import jp.ijufumi.openreports.domain.models.entity.Member
import jp.ijufumi.openreports.domain.port.{AppConfigPort, CachePort, GoogleAuthPort}
import jp.ijufumi.openreports.domain.repository.{MemberRepository, WorkspaceRepository}
import jp.ijufumi.openreports.usecase.port.input.WorkspaceUseCase
import jp.ijufumi.openreports.usecase.port.input.param.LoginInput
import jp.ijufumi.openreports.utils.Hash
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class LoginInteractorSpec extends AnyFlatSpec with MockitoSugar {

  "login" should "return Member if email and password are correct" in {
    // mock
    val db = mock[Database]
    val memberRepository = mock[MemberRepository]
    val workspaceRepository = mock[WorkspaceRepository]
    val googleAuthPort = mock[GoogleAuthPort]
    val workspaceService = mock[WorkspaceUseCase]
    val cachePort = mock[CachePort]
    val appConfig = mock[AppConfigPort]

    val loginService =
      new LoginInteractor(
        db,
        memberRepository,
        workspaceRepository,
        googleAuthPort,
        workspaceService,
        cachePort,
        appConfig,
      )

    val email = "test@test.com"
    val password = "password"
    val input = LoginInput(email, password)
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
    val googleAuthPort = mock[GoogleAuthPort]
    val workspaceService = mock[WorkspaceUseCase]
    val cachePort = mock[CachePort]
    val appConfig = mock[AppConfigPort]

    val loginService =
      new LoginInteractor(
        db,
        memberRepository,
        workspaceRepository,
        googleAuthPort,
        workspaceService,
        cachePort,
        appConfig,
      )

    val email = "test@test.com"
    val password = "password"
    val input = LoginInput(email, password)

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
    val googleAuthPort = mock[GoogleAuthPort]
    val workspaceService = mock[WorkspaceUseCase]
    val cachePort = mock[CachePort]
    val appConfig = mock[AppConfigPort]

    val loginService =
      new LoginInteractor(
        db,
        memberRepository,
        workspaceRepository,
        googleAuthPort,
        workspaceService,
        cachePort,
        appConfig,
      )

    val email = "test@test.com"
    val password = "password"
    val wrongPassword = "wrong_password"
    val input = LoginInput(email, wrongPassword)
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
