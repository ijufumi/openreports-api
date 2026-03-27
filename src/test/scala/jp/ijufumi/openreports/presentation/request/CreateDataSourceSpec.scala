package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CreateDataSourceSpec extends AnyFlatSpec with Matchers {

  val validator: Validator[CreateDataSource] = CreateDataSource.validate

  private def validParam(
      name: String = "test-datasource",
      url: String = "jdbc:postgresql://localhost:5432/test",
      username: String = "user",
      password: String = "pass",
      driverTypeId: String = "driver-type-id",
  ): CreateDataSource = CreateDataSource(name, url, username, password, driverTypeId)

  // 正常系

  "validate" should "return ValidationSuccess when all fields are valid" in {
    validator.validate(validParam()) should be(ValidationSuccess)
  }

  it should "return ValidationSuccess when name is 1 character" in {
    validator.validate(validParam(name = "a")) should be(ValidationSuccess)
  }

  it should "return ValidationSuccess when name is 255 characters" in {
    validator.validate(validParam(name = "a" * 255)) should be(ValidationSuccess)
  }

  // 異常系: name

  it should "return ValidationFailure when name is empty" in {
    val result = validator.validate(validParam(name = ""))
    result shouldBe a[ValidationFailure]
    val failures = result.asInstanceOf[ValidationFailure].violations
    failures should contain(Violation("name must not be empty"))
    failures should contain(Violation("name must be between 1 and 255"))
  }

  it should "return ValidationFailure when name is 256 characters" in {
    val result = validator.validate(validParam(name = "a" * 256))
    result shouldBe a[ValidationFailure]
    result.asInstanceOf[ValidationFailure].violations should contain(
      Violation("name must be between 1 and 255"),
    )
  }

  it should "return ValidationFailure when name is null" in {
    val result = validator.validate(validParam(name = null))
    result shouldBe a[ValidationFailure]
    val failures = result.asInstanceOf[ValidationFailure].violations
    failures should contain(Violation("name must not be empty"))
    failures should contain(Violation("name must be between 1 and 255"))
  }

  // 異常系: url

  it should "return ValidationFailure when url is empty" in {
    val result = validator.validate(validParam(url = ""))
    result shouldBe a[ValidationFailure]
    result.asInstanceOf[ValidationFailure].violations should contain(
      Violation("url must not be empty"),
    )
  }

  // 異常系: username

  it should "return ValidationFailure when username is empty" in {
    val result = validator.validate(validParam(username = ""))
    result shouldBe a[ValidationFailure]
    result.asInstanceOf[ValidationFailure].violations should contain(
      Violation("username must not be empty"),
    )
  }

  // 異常系: password

  it should "return ValidationFailure when password is empty" in {
    val result = validator.validate(validParam(password = ""))
    result shouldBe a[ValidationFailure]
    result.asInstanceOf[ValidationFailure].violations should contain(
      Violation("password must not be empty"),
    )
  }

  // 異常系: driverTypeId

  it should "return ValidationFailure when driverTypeId is empty" in {
    val result = validator.validate(validParam(driverTypeId = ""))
    result shouldBe a[ValidationFailure]
    result.asInstanceOf[ValidationFailure].violations should contain(
      Violation("driverTypeId must not be empty"),
    )
  }

  // 異常系: 複数フィールド

  it should "return ValidationFailure with multiple violations when multiple fields are empty" in {
    val result = validator.validate(validParam(name = "", url = "", username = ""))
    result shouldBe a[ValidationFailure]
    val failures = result.asInstanceOf[ValidationFailure].violations
    failures.size should be >= 4
    failures should contain(Violation("name must not be empty"))
    failures should contain(Violation("url must not be empty"))
    failures should contain(Violation("username must not be empty"))
  }
}
