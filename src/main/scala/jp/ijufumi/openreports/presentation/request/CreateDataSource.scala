package jp.ijufumi.openreports.presentation.request

import jp.ijufumi.openreports.presentation.validation.Validator

case class CreateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)

object CreateDataSource {
  implicit val validate: Validator[CreateDataSource] = new Validator[CreateDataSource] {
    def validate(param: CreateDataSource) = collectViolations(
      notEmpty("name", param.name),
      between("name", param.name.length, 1, 255),
      notEmpty("url", param.url),
      notEmpty("username", param.username),
      notEmpty("password", param.password),
      notEmpty("driverTypeId", param.driverTypeId),
    )
  }
}
