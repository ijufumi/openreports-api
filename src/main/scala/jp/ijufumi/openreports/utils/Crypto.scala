package jp.ijufumi.openreports.utils

import java.nio.charset.StandardCharsets
import java.security.{MessageDigest, SecureRandom}
import java.util.Base64
import javax.crypto.spec.{GCMParameterSpec, SecretKeySpec}
import javax.crypto.{Cipher, SecretKey}

import jp.ijufumi.openreports.configs.Config.HASH_KEY

object Crypto {
  private val ALGORITHM = "AES/GCM/NoPadding"
  private val KEY_ALGORITHM = "AES"
  private val IV_LENGTH_BYTES = 12
  private val TAG_LENGTH_BITS = 128
  private val VERSION_PREFIX = "v1:"

  private val random = new SecureRandom()

  private lazy val secretKey: SecretKey = {
    val digest = MessageDigest.getInstance("SHA-256")
    val keyBytes = digest.digest(HASH_KEY.getBytes(StandardCharsets.UTF_8))
    new SecretKeySpec(keyBytes, KEY_ALGORITHM)
  }

  def encrypt(plainText: String): String = {
    if (plainText == null) return null
    val iv = new Array[Byte](IV_LENGTH_BYTES)
    random.nextBytes(iv)
    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BITS, iv))
    val cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8))
    val combined = new Array[Byte](iv.length + cipherBytes.length)
    System.arraycopy(iv, 0, combined, 0, iv.length)
    System.arraycopy(cipherBytes, 0, combined, iv.length, cipherBytes.length)
    VERSION_PREFIX + Base64.getEncoder.encodeToString(combined)
  }

  def decrypt(cipherText: String): String = {
    if (cipherText == null || cipherText.isEmpty) return cipherText
    if (!cipherText.startsWith(VERSION_PREFIX)) return cipherText
    val combined = Base64.getDecoder.decode(cipherText.substring(VERSION_PREFIX.length))
    val iv = combined.slice(0, IV_LENGTH_BYTES)
    val cipherBytes = combined.slice(IV_LENGTH_BYTES, combined.length)
    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BITS, iv))
    new String(cipher.doFinal(cipherBytes), StandardCharsets.UTF_8)
  }
}
