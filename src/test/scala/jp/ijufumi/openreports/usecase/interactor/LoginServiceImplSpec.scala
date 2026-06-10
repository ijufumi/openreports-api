package jp.ijufumi.openreports.usecase.interactor

import jp.ijufumi.openreports.domain.models.entity.{Member, RefreshToken}
import jp.ijufumi.openreports.domain.port.{AppConfigPort, CachePort, GoogleAuthPort}
import jp.ijufumi.openreports.domain.repository.{
  MemberRepository,
  RefreshTokenRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.usecase.port.input.WorkspaceUseCase
import jp.ijufumi.openreports.usecase.port.input.param.LoginInput
import jp.ijufumi.openreports.utils.{Dates, Hash}
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class LoginInteractorSpec extends AnyFlatSpec with MockitoSugar {

  private class Fixture {
    val db = mock[Database]
    val memberRepository = mock[MemberRepository]
    val workspaceRepository = mock[WorkspaceRepository]
    val refreshTokenRepository = mock[RefreshTokenRepository]
    val googleAuthPort = mock[GoogleAuthPort]
    val workspaceService = mock[WorkspaceUseCase]
    val cachePort = mock[CachePort]
    val appConfig = mock[AppConfigPort]

    val loginService =
      new LoginInteractor(
        db,
        memberRepository,
        workspaceRepository,
        refreshTokenRepository,
        googleAuthPort,
        workspaceService,
        cachePort,
        appConfig,
      )
  }

  "login" should "return Member if email and password are correct" in {
    val f = new Fixture

    val email = "test@test.com"
    val password = "password"
    val input = LoginInput(email, password)
    val member = Member(
      id = "1",
      googleId = None,
      email = email,
      password = Hash.hashPassword(password),
      name = "test",
      createdAt = 0,
      updatedAt = 0,
    )

    when(f.memberRepository.getMemberByEmail(f.db, email)).thenReturn(Some(member))
    when(f.workspaceRepository.getsByMemberId(f.db, member.id)).thenReturn(Seq.empty)

    // when
    val actual = f.loginService.login(input)

    // then
    assert(actual.isDefined)
    assert(actual.get.email == email)
  }

  it should "return None if email does not exist" in {
    val f = new Fixture

    val email = "test@test.com"
    val password = "password"
    val input = LoginInput(email, password)

    when(f.memberRepository.getMemberByEmail(f.db, email)).thenReturn(None)

    // when
    val actual = f.loginService.login(input)

    // then
    assert(actual.isEmpty)
  }

  it should "return None if password does not match" in {
    val f = new Fixture

    val email = "test@test.com"
    val password = "password"
    val wrongPassword = "wrong_password"
    val input = LoginInput(email, wrongPassword)
    val member = Member(
      id = "1",
      googleId = None,
      email = email,
      password = Hash.hashPassword(password),
      name = "test",
      createdAt = 0,
      updatedAt = 0,
    )

    when(f.memberRepository.getMemberByEmail(f.db, email)).thenReturn(Some(member))

    // when
    val actual = f.loginService.login(input)

    // then
    assert(actual.isEmpty)
  }

  "generateTokens" should "return tokens and register refresh token" in {
    val f = new Fixture
    val memberId = "member-1"

    when(f.appConfig.accessTokenExpirationSec).thenReturn(Integer.valueOf(600))
    when(f.appConfig.refreshTokenExpirationSec).thenReturn(Integer.valueOf(3600))

    // when
    val tokens = f.loginService.generateTokens(memberId)

    // then
    assert(Hash.extractIdFromJWT(tokens.accessToken) == memberId)
    assert(tokens.refreshToken.isDefined)
    assert(Hash.extractIdFromJWT(tokens.refreshToken.get) == memberId)
    verify(f.refreshTokenRepository).deleteExpired(
      ArgumentMatchers.eq(f.db),
      ArgumentMatchers.anyLong(),
    )
    verify(f.refreshTokenRepository).register(
      ArgumentMatchers.eq(f.db),
      ArgumentMatchers.any[RefreshToken](),
    )
  }

  "refreshTokens" should "return new tokens and rotate refresh token" in {
    val f = new Fixture
    val memberId = "member-1"

    when(f.appConfig.accessTokenExpirationSec).thenReturn(Integer.valueOf(600))
    when(f.appConfig.refreshTokenExpirationSec).thenReturn(Integer.valueOf(3600))

    val refreshToken = Hash.generateJWT(memberId, 3600)
    val hashedToken = Hash.hmacSha256(refreshToken)
    val storedToken = RefreshToken(
      id = "token-1",
      memberId = memberId,
      refreshToken = hashedToken,
      expiredAt = Dates.currentTimestamp() + 3600 * 1000,
    )

    when(f.refreshTokenRepository.getByToken(f.db, hashedToken)).thenReturn(Some(storedToken))
    when(
      f.refreshTokenRepository.markUsed(
        ArgumentMatchers.eq(f.db),
        ArgumentMatchers.eq(hashedToken),
        ArgumentMatchers.anyLong(),
      ),
    ).thenReturn(1)

    // when
    val actual = f.loginService.refreshTokens(refreshToken)

    // then
    assert(actual.isDefined)
    assert(Hash.extractIdFromJWT(actual.get.accessToken) == memberId)
    assert(actual.get.refreshToken.isDefined)
    assert(Hash.extractIdFromJWT(actual.get.refreshToken.get) == memberId)
  }

  it should "reissue only access token if token was already used within grace period" in {
    val f = new Fixture
    val memberId = "member-1"
    val refreshToken = Hash.generateJWT(memberId, 3600)
    val hashedToken = Hash.hmacSha256(refreshToken)
    val usedToken = RefreshToken(
      id = "token-1",
      memberId = memberId,
      refreshToken = hashedToken,
      expiredAt = Dates.currentTimestamp() + 3600 * 1000,
      usedAt = Some(Dates.currentTimestamp() - 1000),
    )

    when(f.appConfig.accessTokenExpirationSec).thenReturn(Integer.valueOf(600))
    when(f.refreshTokenRepository.getByToken(f.db, hashedToken)).thenReturn(Some(usedToken))
    // another request already used the token
    when(
      f.refreshTokenRepository.markUsed(
        ArgumentMatchers.eq(f.db),
        ArgumentMatchers.eq(hashedToken),
        ArgumentMatchers.anyLong(),
      ),
    ).thenReturn(0)

    // when
    val actual = f.loginService.refreshTokens(refreshToken)

    // then
    assert(actual.isDefined)
    assert(Hash.extractIdFromJWT(actual.get.accessToken) == memberId)
    assert(actual.get.refreshToken.isEmpty)
    verify(f.refreshTokenRepository, never()).register(
      ArgumentMatchers.eq(f.db),
      ArgumentMatchers.any[RefreshToken](),
    )
  }

  it should "return None if token was already used and grace period has passed" in {
    val f = new Fixture
    val memberId = "member-1"
    val refreshToken = Hash.generateJWT(memberId, 3600)
    val hashedToken = Hash.hmacSha256(refreshToken)
    val usedToken = RefreshToken(
      id = "token-1",
      memberId = memberId,
      refreshToken = hashedToken,
      expiredAt = Dates.currentTimestamp() + 3600 * 1000,
      usedAt = Some(Dates.currentTimestamp() - 10000),
    )

    when(f.refreshTokenRepository.getByToken(f.db, hashedToken)).thenReturn(Some(usedToken))
    when(
      f.refreshTokenRepository.markUsed(
        ArgumentMatchers.eq(f.db),
        ArgumentMatchers.eq(hashedToken),
        ArgumentMatchers.anyLong(),
      ),
    ).thenReturn(0)

    // when
    val actual = f.loginService.refreshTokens(refreshToken)

    // then
    assert(actual.isEmpty)
  }

  it should "return None if refresh token is not a valid JWT" in {
    val f = new Fixture

    // when
    val actual = f.loginService.refreshTokens("invalid-token")

    // then
    assert(actual.isEmpty)
  }

  it should "return None if refresh token is not registered" in {
    val f = new Fixture
    val refreshToken = Hash.generateJWT("member-1", 3600)

    when(f.refreshTokenRepository.getByToken(f.db, Hash.hmacSha256(refreshToken)))
      .thenReturn(None)

    // when
    val actual = f.loginService.refreshTokens(refreshToken)

    // then
    assert(actual.isEmpty)
  }

  it should "return None and delete token if refresh token is expired" in {
    val f = new Fixture
    val memberId = "member-1"
    val refreshToken = Hash.generateJWT(memberId, 3600)
    val hashedToken = Hash.hmacSha256(refreshToken)
    val storedToken = RefreshToken(
      id = "token-1",
      memberId = memberId,
      refreshToken = hashedToken,
      expiredAt = Dates.currentTimestamp() - 1000,
    )

    when(f.refreshTokenRepository.getByToken(f.db, hashedToken)).thenReturn(Some(storedToken))

    // when
    val actual = f.loginService.refreshTokens(refreshToken)

    // then
    assert(actual.isEmpty)
    verify(f.refreshTokenRepository).deleteByToken(f.db, hashedToken)
  }

  "logout" should "delete all refresh tokens of the session" in {
    val f = new Fixture
    val memberId = "member-1"
    // distinct expirations so the two JWTs are not identical
    val headerToken = Hash.generateJWT(memberId, 3600)
    val rotatedToken = Hash.generateJWT(memberId, 7200)

    // when
    f.loginService.logout(memberId, Seq(headerToken, rotatedToken))

    // then
    verify(f.refreshTokenRepository).deleteByToken(f.db, Hash.hmacSha256(headerToken))
    verify(f.refreshTokenRepository).deleteByToken(f.db, Hash.hmacSha256(rotatedToken))
    verify(f.refreshTokenRepository, never()).deleteByMemberId(f.db, memberId)
  }

  it should "not delete refresh token if it belongs to another member" in {
    val f = new Fixture
    val refreshToken = Hash.generateJWT("member-2", 3600)

    // when
    f.loginService.logout("member-1", Seq(refreshToken))

    // then
    verify(f.refreshTokenRepository, never()).deleteByToken(
      ArgumentMatchers.eq(f.db),
      ArgumentMatchers.anyString(),
    )
  }

  it should "do nothing if refresh tokens are empty" in {
    val f = new Fixture

    // when
    f.loginService.logout("member-1", Seq.empty)

    // then
    verify(f.refreshTokenRepository, never()).deleteByToken(
      ArgumentMatchers.eq(f.db),
      ArgumentMatchers.anyString(),
    )
  }
}
