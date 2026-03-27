package jp.ijufumi.openreports.presentation.validation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestValidator extends Validator[String] {
  override def validate(value: String): ValidationResult = ValidationSuccess

  // Expose protected methods for testing
  def testCheck(condition: Boolean, message: String): Option[Violation] =
    check(condition, message)

  def testNotEmpty(fieldName: String, value: String): Option[Violation] =
    notEmpty(fieldName, value)

  def testBetween(fieldName: String, value: Int, min: Int, max: Int): Option[Violation] =
    between(fieldName, value, min, max)

  def testLengthBetween(fieldName: String, value: String, min: Int, max: Int): Option[Violation] =
    lengthBetween(fieldName, value, min, max)

  def testCollectViolations(checks: Option[Violation]*): ValidationResult =
    collectViolations(checks: _*)
}

class ValidatorSpec extends AnyFlatSpec with Matchers {

  val validator = new TestValidator()

  // check

  "check" should "return None when condition is true" in {
    validator.testCheck(true, "error message") should be(None)
  }

  it should "return Some(Violation) when condition is false" in {
    validator.testCheck(false, "error message") should be(Some(Violation("error message")))
  }

  // notEmpty

  "notEmpty" should "return None for non-empty string" in {
    validator.testNotEmpty("name", "value") should be(None)
  }

  it should "return Violation for empty string" in {
    validator.testNotEmpty("name", "") should be(Some(Violation("name must not be empty")))
  }

  it should "return Violation for null" in {
    validator.testNotEmpty("name", null) should be(Some(Violation("name must not be empty")))
  }

  // between

  "between" should "return None when value is within range" in {
    validator.testBetween("age", 5, 1, 10) should be(None)
  }

  it should "return None when value equals min" in {
    validator.testBetween("age", 1, 1, 10) should be(None)
  }

  it should "return None when value equals max" in {
    validator.testBetween("age", 10, 1, 10) should be(None)
  }

  it should "return Violation when value is below min" in {
    validator.testBetween("age", 0, 1, 10) should be(
      Some(Violation("age must be between 1 and 10")),
    )
  }

  it should "return Violation when value is above max" in {
    validator.testBetween("age", 11, 1, 10) should be(
      Some(Violation("age must be between 1 and 10")),
    )
  }

  // lengthBetween

  "lengthBetween" should "return None when string length is within range" in {
    validator.testLengthBetween("name", "abc", 1, 10) should be(None)
  }

  it should "return None when string length equals min" in {
    validator.testLengthBetween("name", "a", 1, 10) should be(None)
  }

  it should "return None when string length equals max" in {
    validator.testLengthBetween("name", "a" * 10, 1, 10) should be(None)
  }

  it should "return Violation when string length is below min" in {
    validator.testLengthBetween("name", "", 1, 10) should be(
      Some(Violation("name must be between 1 and 10")),
    )
  }

  it should "return Violation when string length is above max" in {
    validator.testLengthBetween("name", "a" * 11, 1, 10) should be(
      Some(Violation("name must be between 1 and 10")),
    )
  }

  it should "treat null as length 0" in {
    validator.testLengthBetween("name", null, 1, 10) should be(
      Some(Violation("name must be between 1 and 10")),
    )
  }

  it should "return None when null and min is 0" in {
    validator.testLengthBetween("name", null, 0, 10) should be(None)
  }

  // collectViolations

  "collectViolations" should "return ValidationSuccess when all checks are None" in {
    validator.testCollectViolations(None, None) should be(ValidationSuccess)
  }

  it should "return ValidationSuccess when no checks are given" in {
    validator.testCollectViolations() should be(ValidationSuccess)
  }

  it should "return ValidationFailure with single violation" in {
    val violation = Violation("error")
    validator.testCollectViolations(Some(violation)) should be(
      ValidationFailure(Seq(violation)),
    )
  }

  it should "return ValidationFailure with multiple violations" in {
    val v1 = Violation("error1")
    val v2 = Violation("error2")
    validator.testCollectViolations(Some(v1), Some(v2)) should be(
      ValidationFailure(Seq(v1, v2)),
    )
  }

  it should "return ValidationFailure with only violations from mixed checks" in {
    val violation = Violation("error")
    validator.testCollectViolations(None, Some(violation), None) should be(
      ValidationFailure(Seq(violation)),
    )
  }
}
