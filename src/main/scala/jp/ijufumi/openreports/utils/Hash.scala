package jp.ijufumi.openreports.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Hex
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException

import java.util.{Calendar, Date}
import jp.ijufumi.openreports.config.Config.HASH_KEY

object Hash {
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
      decoded.getClaim("id").asString()
    } catch {
      case _: JWTDecodeException => return ""
    }
  }
}
