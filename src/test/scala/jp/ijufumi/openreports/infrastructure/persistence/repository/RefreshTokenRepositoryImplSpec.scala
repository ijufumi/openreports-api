package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.RefreshToken
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.utils.{Dates, IDs}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database

class RefreshTokenRepositoryImplSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  var db: Database = _
  val repository = new RefreshTokenRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("refresh_token_test")
    H2DatabaseHelper.createSchema(db, refreshTokenQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, refreshTokenQuery)
  }

  private def newToken(
      memberId: String = "member-1",
      refreshToken: String = IDs.ulid(),
      expiredAt: Long = Dates.currentTimestamp() + 3600 * 1000,
  ): RefreshToken = {
    RefreshToken(
      id = IDs.ulid(),
      memberId = memberId,
      refreshToken = refreshToken,
      expiredAt = expiredAt,
    )
  }

  "register" should "create new refresh token and return it" in {
    val token = newToken()

    val result = repository.register(db, token)

    result should be(defined)
    result.get.id should equal(token.id)
    result.get.memberId should equal(token.memberId)
    result.get.refreshToken should equal(token.refreshToken)
    result.get.expiredAt should equal(token.expiredAt)
  }

  "getByToken" should "return refresh token when exists" in {
    val token = newToken()
    repository.register(db, token)

    val result = repository.getByToken(db, token.refreshToken)

    result should be(defined)
    result.get.id should equal(token.id)
  }

  it should "return None when refresh token doesn't exist" in {
    val result = repository.getByToken(db, "non-existent-token")

    result should be(None)
  }

  "markUsed" should "mark the refresh token as used and return updated count" in {
    val token = newToken()
    repository.register(db, token)
    val usedAt = Dates.currentTimestamp()

    val updated = repository.markUsed(db, token.refreshToken, usedAt)

    updated should equal(1)
    repository.getByToken(db, token.refreshToken).get.usedAt should equal(Some(usedAt))
  }

  it should "return 0 when refresh token is already used" in {
    val token = newToken()
    repository.register(db, token)
    repository.markUsed(db, token.refreshToken, Dates.currentTimestamp())

    val updated = repository.markUsed(db, token.refreshToken, Dates.currentTimestamp())

    updated should equal(0)
  }

  it should "return 0 when refresh token doesn't exist" in {
    val updated = repository.markUsed(db, "non-existent-token", Dates.currentTimestamp())

    updated should equal(0)
  }

  "deleteByToken" should "delete the refresh token and return deleted count" in {
    val token = newToken()
    repository.register(db, token)

    val deleted = repository.deleteByToken(db, token.refreshToken)

    deleted should equal(1)
    repository.getByToken(db, token.refreshToken) should be(None)
  }

  it should "return 0 when refresh token doesn't exist" in {
    val deleted = repository.deleteByToken(db, "non-existent-token")

    deleted should equal(0)
  }

  "deleteByMemberId" should "delete all refresh tokens of the member" in {
    val token1 = newToken(memberId = "member-1")
    val token2 = newToken(memberId = "member-1")
    val token3 = newToken(memberId = "member-2")
    repository.register(db, token1)
    repository.register(db, token2)
    repository.register(db, token3)

    repository.deleteByMemberId(db, "member-1")

    repository.getByToken(db, token1.refreshToken) should be(None)
    repository.getByToken(db, token2.refreshToken) should be(None)
    repository.getByToken(db, token3.refreshToken) should be(defined)
  }

  "deleteExpired" should "delete only expired refresh tokens" in {
    val now = Dates.currentTimestamp()
    val expiredToken = newToken(expiredAt = now - 1000)
    val validToken = newToken(expiredAt = now + 3600 * 1000)
    repository.register(db, expiredToken)
    repository.register(db, validToken)

    repository.deleteExpired(db, now)

    repository.getByToken(db, expiredToken.refreshToken) should be(None)
    repository.getByToken(db, validToken.refreshToken) should be(defined)
  }
}
