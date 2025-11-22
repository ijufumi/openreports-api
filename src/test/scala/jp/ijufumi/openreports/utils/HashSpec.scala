package jp.ijufumi.openreports.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.util.{Calendar, Date}

class HashSpec extends AnyFlatSpec with Matchers {

  "hmacSha256" should "generate consistent hashes for same input" in {
    val input = "test-value"
    val hash1 = Hash.hmacSha256(input)
    val hash2 = Hash.hmacSha256(input)

    hash1 should equal(hash2)
  }

  it should "produce different hashes for different inputs" in {
    val hash1 = Hash.hmacSha256("value1")
    val hash2 = Hash.hmacSha256("value2")

    hash1 should not equal hash2
  }

  it should "produce different hashes with different salts" in {
    val input = "test-value"
    val hash1 = Hash.hmacSha256(input, "salt1")
    val hash2 = Hash.hmacSha256(input, "salt2")

    hash1 should not equal hash2
  }

  it should "generate 64-character hex string" in {
    val hash = Hash.hmacSha256("test")

    hash should have length 64
    hash should fullyMatch regex "[0-9a-f]+"
  }

  it should "handle empty string" in {
    noException should be thrownBy Hash.hmacSha256("")
  }

  it should "handle special characters" in {
    val specialChars = "!@#$%^&*()[]{}|\\;:'\",.<>?/~`"
    noException should be thrownBy Hash.hmacSha256(specialChars)
  }

  it should "handle unicode characters" in {
    val unicode = "こんにちは世界"
    noException should be thrownBy Hash.hmacSha256(unicode)
  }

  "generateJWT" should "create valid JWT token" in {
    val id = "test-user-id"
    val token = Hash.generateJWT(id, 3600)

    token should not be empty
    noException should be thrownBy JWT.decode(token)
  }

  it should "include correct id claim" in {
    val id = "test-user-id"
    val token = Hash.generateJWT(id, 3600)

    val decoded = JWT.decode(token)
    decoded.getClaim("id").asString() should equal(id)
  }

  it should "include issuer claim" in {
    val token = Hash.generateJWT("test-id", 3600)

    val decoded = JWT.decode(token)
    decoded.getIssuer should equal("openreports")
  }

  it should "set expiration time correctly" in {
    val expirationSeconds = 3600
    val beforeGeneration = System.currentTimeMillis()
    java.lang.Thread.sleep(1000)
    val token = Hash.generateJWT("test-id", expirationSeconds)
    val afterGeneration = System.currentTimeMillis()

    val decoded = JWT.decode(token)
    val expiresAt = decoded.getExpiresAt.getTime

    // Should expire approximately expirationSeconds from now
    val expectedMin = beforeGeneration + (expirationSeconds * 1000)
    val expectedMax = afterGeneration + (expirationSeconds * 1000)

    expiresAt should (be >= expectedMin and be <= expectedMax)
  }

  it should "set issued at time" in {
    val beforeGeneration = System.currentTimeMillis()
    java.lang.Thread.sleep(1000)
    val token = Hash.generateJWT("test-id", 3600)
    val afterGeneration = System.currentTimeMillis()

    val decoded = JWT.decode(token)
    val issuedAt = decoded.getIssuedAt.getTime

    issuedAt should (be >= beforeGeneration and be <= afterGeneration)
  }

  it should "handle very short expiration times" in {
    val token = Hash.generateJWT("test-id", 1)

    noException should be thrownBy JWT.decode(token)
  }

  it should "handle very long expiration times" in {
    val token = Hash.generateJWT("test-id", 86400 * 365) // 1 year

    noException should be thrownBy JWT.decode(token)
  }

  "extractIdFromJWT" should "decode valid token and extract id" in {
    val expectedId = "test-user-id"
    val token = Hash.generateJWT(expectedId, 3600)

    val actualId = Hash.extractIdFromJWT(token)

    actualId should equal(expectedId)
  }

  it should "return empty string for expired token" in {
    val token = Hash.generateJWT("test-id", -3600) // Already expired

    val result = Hash.extractIdFromJWT(token)

    result should equal("")
  }

  it should "return empty string for malformed token" in {
    val malformedToken = "not.a.valid.jwt.token"

    val result = Hash.extractIdFromJWT(malformedToken)

    result should equal("")
  }

  it should "return empty string for completely invalid token" in {
    val invalidToken = "totally-invalid"

    val result = Hash.extractIdFromJWT(invalidToken)

    result should equal("")
  }

  it should "return empty string for empty string" in {
    val result = Hash.extractIdFromJWT("")

    result should equal("")
  }

  it should "handle token with special characters in id" in {
    val specialId = "user-123_test@example.com"
    val token = Hash.generateJWT(specialId, 3600)

    val result = Hash.extractIdFromJWT(token)

    result should equal(specialId)
  }

  it should "handle token with unicode characters in id" in {
    val unicodeId = "ユーザー123"
    val token = Hash.generateJWT(unicodeId, 3600)

    val result = Hash.extractIdFromJWT(token)

    result should equal(unicodeId)
  }

  "isExpired" should "return false for non-expired token" in {
    val token = Hash.generateJWT("test-id", 3600)
    val decoded = JWT.decode(token)

    Hash.isExpired(decoded) should be(false)
  }

  it should "return true for expired token" in {
    val token = Hash.generateJWT("test-id", -3600) // Already expired
    val decoded = JWT.decode(token)

    Hash.isExpired(decoded) should be(true)
  }

  it should "return true for token expiring now" in {
    // Create a token that expires immediately
    val cal = Calendar.getInstance()
    cal.add(Calendar.SECOND, -1) // 1 second ago

    val token = Hash.generateJWT("test-id", -1)
    val decoded = JWT.decode(token)

    Hash.isExpired(decoded) should be(true)
  }

  it should "return false for token expiring in the future" in {
    val token = Hash.generateJWT("test-id", 60) // 1 minute from now
    val decoded = JWT.decode(token)

    // Small delay to ensure time passes
    Thread.sleep(10)

    Hash.isExpired(decoded) should be(false)
  }

  "Hash object" should "be consistent across multiple operations" in {
    val id = "consistent-test-id"
    val password = "test-password"

    // Generate hash
    val passwordHash = Hash.hmacSha256(password)

    // Generate token
    val token = Hash.generateJWT(id, 3600)

    // Extract id
    val extractedId = Hash.extractIdFromJWT(token)

    // Verify consistency
    extractedId should equal(id)
    Hash.hmacSha256(password) should equal(passwordHash)
  }
}
