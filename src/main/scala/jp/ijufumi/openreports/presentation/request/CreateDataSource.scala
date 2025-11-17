package jp.ijufumi.openreports.presentation.request

case class CreateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
