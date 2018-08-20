package jp.ijufumi.openreports.controller.validator

import skinny.validator.ValidationRule

object password extends password(10, true, true, true, true)

case class password(
    minLength: Int = 8,
    needsNumber: Boolean = true,
    needsLowerAlpha: Boolean = true,
    needsUpperAlpha: Boolean = true,
    needsSymbol: Boolean = true,
) extends ValidationRule {
  def name = "password"
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
