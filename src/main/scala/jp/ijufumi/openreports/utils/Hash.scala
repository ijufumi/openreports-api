package jp.ijufumi.openreports.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Hex
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT

import java.util.{Calendar, Date}
import jp.ijufumi.openreports.configs.Config.HASH_KEY

object Hash extends Logging {
  def hmacSha256(value: String, salt: String = HASH_KEY): String = {
    val spec = new SecretKeySpec(salt.getBytes, "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(spec)
    val bytes = mac.doFinal(value.getBytes)

    Hex.encodeHexString(bytes)
  }
  def generateJWT(id: String, expirationSeconds: Integer): String = {
    val algorithm = Algorithm.HMAC512("secret")
    val cal = Calendar.getInstance()
    cal.add(Calendar.SECOND, expirationSeconds)
    JWT
      .create()
      .withIssuer("openreports")
      .withExpiresAt(new Date(cal.getTimeInMillis))
      .withIssuedAt(new Date)
      .withClaim("id", id)
      .sign(algorithm)
  }

  def extractIdFromJWT(jwtString: String): String = {
    try {
      val decoded = JWT.decode(jwtString)
      if (isExpired(decoded)) {
        return ""
      }
      decoded.getClaim("id").asString()
    } catch {
      case e: JWTDecodeException =>
        logger.warn("Failed to extract id from JWT", e)
        ""
    }
  }

  def isExpired(decoded: DecodedJWT): Boolean = {
    val expiredAt = decoded.getExpiresAt
    expiredAt.before(new Date())
  }
}
