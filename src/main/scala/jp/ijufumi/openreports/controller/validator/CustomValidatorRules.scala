package jp.ijufumi.openreports.controller.validator

import skinny.validator.ValidationRule

object password extends ValidationRule {
  def name = "password"
  def isValid(value:Any):Boolean = isEmpty(value) || {
    val strValue = value.toString
    strValue.length > 8 && strValue.matches(".*\d.*") &&
      strValue.matches(".*[a-z].*") && strValue.matches(".*[A-Z].*")
  }
}
