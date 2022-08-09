package jp.ijufumi.openreports.utils

import scala.collection.mutable
import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.net.URLCodec

object Strings {
  private val LOWER_CASE = "abcdefghijklmnopqrstuvwxyz"
  private val UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  private val NUMERIC = "0123456789"

  private val urlCodec = new URLCodec()

  def generateRandomSting(
      count: Int,
      useLower: Boolean = true,
      useUpper: Boolean = true,
      useNumeric: Boolean = true,
  ): String = {
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

    val r = new Random()
    val baseString = baseStringBuilder.toString()
    for (i <- 0 until count) {
      val index = r.between(0, baseString.length())
      builder ++= baseString.charAt(index).toString
    }
    builder.mkString
  }

  def generateQueryParamsFromMap(values: mutable.Map[String, Any]): String = {
    val builder = new mutable.StringBuilder
    var index = 0
    values.keys.foreach(k => {
      builder ++= s"${k}=${urlCodec.encode(values.getOrElse(k, ""))}"
      if (index != values.size - 1) {
        builder ++= "&"
      }
      index += 1
    })
    builder.mkString
  }

  def convertToBase64(value: String): String = {
    Base64.encodeBase64String(value.getBytes())
  }

  def convertFromBase64(value: String): String = {
    Base64.decodeBase64(value).mkString
  }
}
