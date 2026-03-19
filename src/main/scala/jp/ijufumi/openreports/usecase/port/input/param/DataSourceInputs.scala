package jp.ijufumi.openreports.usecase.port.input.param

case class CreateDataSourceInput(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)

case class UpdateDataSourceInput(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
