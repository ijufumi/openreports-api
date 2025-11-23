package jp.ijufumi.openreports.presentation.request

import com.wix.accord.Validator
import com.wix.accord.dsl._

case class CreateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)

object CreateDataSource {
  implicit val validate: Validator[CreateDataSource] = validator[CreateDataSource] { param =>
    param.name is notEmpty
    param.name.length is between(1, 255)
    param.url is notEmpty
    param.username is notEmpty
    param.password is notEmpty
    param.driverTypeId is notEmpty
  }
}
