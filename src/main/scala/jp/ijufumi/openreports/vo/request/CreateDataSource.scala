package jp.ijufumi.openreports.vo.request

case class CreateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
