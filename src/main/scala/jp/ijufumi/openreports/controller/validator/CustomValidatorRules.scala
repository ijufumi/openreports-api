package jp.ijufumi.openreports.controller.validator

import skinny.validator.ValidationRule

import scala.collection.mutable

object password extends password(10, true, true, true, true)

case class password(
    minLength: Int = 8,
    needsNumber: Boolean = true,
    needsLowerAlpha: Boolean = true,
    needsUpperAlpha: Boolean = true,
    needsSymbol: Boolean = true,
) extends ValidationRule {
  def name = "password"
  override def messageParams = {
    val list: mutable.ArraySeq[String] = mutable.ArraySeq.empty
    if (needsNumber) {
      list + "0 - 9"
    }
    if (needsLowerAlpha) {
      list + "a - z"
    }
    if (needsUpperAlpha) {
      list + "a - Z"
    }
    if (needsSymbol) {
      list + "a - z"
    }
    Seq(minLength, list.mkString(","))
  }
  def isValid(value: Any): Boolean = isEmpty(value) || {
    val strValue = value.toString
    strValue.length > minLength && {
      if (needsNumber) {
        strValue.matches(".*\\d.*")
      } else {
        true
      }
    } && {
      if (needsLowerAlpha) {
        strValue.matches(".*[a-z].*")
      } else {
        true
      }
    } && {
      if (needsUpperAlpha) {
        strValue.matches(".*[A-Z].*")
      } else {
        true
      }
    } && {
      if (needsSymbol) {
        strValue.matches(".*[!-/].*")
      } else {
        true
      }
    }
  }
}
