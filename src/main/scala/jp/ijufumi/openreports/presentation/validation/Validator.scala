package jp.ijufumi.openreports.presentation.validation

case class Violation(message: String)

sealed trait ValidationResult
case object ValidationSuccess extends ValidationResult
case class ValidationFailure(violations: Seq[Violation]) extends ValidationResult

trait Validator[T] {
  def validate(value: T): ValidationResult

  protected def check(
      condition: Boolean,
      message: String,
  ): Option[Violation] = {
    if (condition) None else Some(Violation(message))
  }

  protected def notEmpty(fieldName: String, value: String): Option[Violation] = {
    // value can be null when deserialized from JSON via json4s Java interop
    check(value != null && value.nonEmpty, s"$fieldName must not be empty")
  }

  protected def between(
      fieldName: String,
      value: Int,
      min: Int,
      max: Int,
  ): Option[Violation] = {
    check(value >= min && value <= max, s"$fieldName must be between $min and $max")
  }

  protected def lengthBetween(
      fieldName: String,
      value: String,
      min: Int,
      max: Int,
  ): Option[Violation] = {
    val len = if (value == null) 0 else value.length
    between(fieldName, len, min, max)
  }

  protected def collectViolations(checks: Option[Violation]*): ValidationResult = {
    val violations = checks.flatten.toSeq
    if (violations.isEmpty) ValidationSuccess
    else ValidationFailure(violations)
  }
}
