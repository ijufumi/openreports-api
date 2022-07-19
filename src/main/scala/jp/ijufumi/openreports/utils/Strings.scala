package jp.ijufumi.openreports.utils

import scala.collection.mutable
import scala.util.Random
import scala.collection.mutable.ArrayBuffer

object Strings {
  private val LOWER_CASE = "abcdefghijklmnopqrstuvwxyz"
  private val UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  private val NUMERIC = "0123456789"

  def generateRandomSting(
      count: Int,
      useLower: Boolean = true,
      useUpper: Boolean = true,
      useNumeric: Boolean = true,
  ): String = {
    val baseArray = ArrayBuffer()
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
      baseArray + baseString.charAt(index).toString
    }
    baseArray.mkString
  }
}
