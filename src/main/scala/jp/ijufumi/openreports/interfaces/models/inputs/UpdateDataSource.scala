package jp.ijufumi.openreports.interfaces.models.inputs

case class UpdateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
