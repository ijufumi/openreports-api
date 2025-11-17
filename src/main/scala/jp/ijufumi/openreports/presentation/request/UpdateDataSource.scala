package jp.ijufumi.openreports.presentation.request

case class UpdateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
