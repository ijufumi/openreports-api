package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.{ValidationResult, Validator}

case class CreateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)

object CreateDataSource {
  implicit val validate: Validator[CreateDataSource] = new Validator[CreateDataSource] {
    def validate(param: CreateDataSource): ValidationResult = collectViolations(
      notEmpty("name", param.name),
      lengthBetween("name", param.name, 1, 255),
      notEmpty("url", param.url),
      notEmpty("username", param.username),
      notEmpty("password", param.password),
      notEmpty("driverTypeId", param.driverTypeId),
    )
  }
}
