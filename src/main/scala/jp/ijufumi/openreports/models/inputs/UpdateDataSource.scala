package jp.ijufumi.openreports.models.inputs

case class UpdateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
