package jp.ijufumi.openreports.service.support

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Hex

object Hash {
  def hmacSha256(
      salt: String,
      value: String
  ) = {
    val spec = new SecretKeySpec(salt.getBytes, "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(spec)
    val bytes = mac.doFinal(value.getBytes)

    Hex.encodeHexString(bytes)
  }
}
