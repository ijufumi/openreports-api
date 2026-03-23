package jp.ijufumi.openreports.utils

import scala.collection.mutable
import scala.util.Random
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.net.URLCodec

object Strings {
  private val LOWER_CASE = "abcdefghijklmnopqrstuvwxyz"
  private val UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  private val NUMERIC = "0123456789"

  private val urlCodec = new URLCodec()

  private val emailMatcher = """([^@]*)@.*""".r

  def generateRandomString(
      count: Int,
      useLower: Boolean = true,
      useUpper: Boolean = true,
      useNumeric: Boolean = true,
  )(extraCharacters: String*): String = {
    val builder = new mutable.StringBuilder
    val baseStringBuilder = new mutable.StringBuilder()
    if (useLower) {
      baseStringBuilder ++= LOWER_CASE
    }
    if (useUpper) {
      baseStringBuilder ++= UPPER_CASE
    }
    if (useNumeric) {
      baseStringBuilder ++= NUMERIC
    }
    if (extraCharacters.nonEmpty) {
      baseStringBuilder ++= extraCharacters.mkString
    }

    val baseString = baseStringBuilder.toString()
    val r = new Random()
    for (i <- 0 until count) {
      val index = r.nextInt(baseString.length())
      builder ++= baseString.charAt(index).toString
    }
    builder.mkString
  }

  def generateQueryParamsFromMap(values: mutable.Map[String, String]): String = {
    values
      .map { case (k, v) => s"${k}=${urlCodec.encode(v)}" }
      .mkString("&")
  }

  def convertToBase64(value: String): String = {
    Base64.encodeBase64String(value.getBytes())
  }

  def convertFromBase64(value: String): String = {
    new String(Base64.decodeBase64(value.getBytes()))
  }

  def generateSlug(): String = {
    generateRandomString(10, useUpper = false)()
  }

  def nameFromEmail(email: String): String = {
    val result = emailMatcher.findFirstMatchIn(email)
    if (result.isEmpty) {
      return email
    }
    result.get.group(1)
  }

  def extension(filename: String): String = {
    val index = filename.lastIndexOf(".")
    if (index == -1) {
      return ""
    }
    filename.substring(index)
  }
}
