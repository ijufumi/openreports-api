package jp.ijufumi.openreports.utils

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.apache.commons.codec.binary.Hex

import java.nio.charset.StandardCharsets
import java.util.{Calendar, Date}
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import jp.ijufumi.openreports.configs.Config.HASH_KEY

object Hash extends Logging {
  private val ISSUER = "openreports"
  private val BCRYPT_COST = 12

  private lazy val jwtAlgorithm: Algorithm = Algorithm.HMAC512(HASH_KEY)
  private lazy val jwtVerifier = JWT.require(jwtAlgorithm).withIssuer(ISSUER).build()

  def hmacSha256(value: String, salt: String = HASH_KEY): String = {
    val spec = new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(spec)
    val bytes = mac.doFinal(value.getBytes(StandardCharsets.UTF_8))

    Hex.encodeHexString(bytes)
  }

  def hashPassword(password: String): String = {
    BCrypt.withDefaults().hashToString(BCRYPT_COST, password.toCharArray)
  }

  def verifyPassword(password: String, hashed: String): Boolean = {
    if (password == null || hashed == null || hashed.isEmpty) return false
    BCrypt.verifyer().verify(password.toCharArray, hashed).verified
  }

  def generateJWT(id: String, expirationSeconds: Integer): String = {
    val cal = Calendar.getInstance()
    cal.add(Calendar.SECOND, expirationSeconds)
    JWT
      .create()
      .withIssuer(ISSUER)
      .withExpiresAt(new Date(cal.getTimeInMillis))
      .withIssuedAt(new Date)
      .withClaim("id", id)
      .sign(jwtAlgorithm)
  }

  def extractIdFromJWT(jwtString: String): String = {
    try {
      val decoded = jwtVerifier.verify(jwtString)
      decoded.getClaim("id").asString()
    } catch {
      case e: JWTVerificationException =>
        logger.warn("Failed to verify JWT", e)
        ""
    }
  }
}
