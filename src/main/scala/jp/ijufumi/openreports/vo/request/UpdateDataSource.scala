package jp.ijufumi.openreports.vo.request

case class UpdateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
