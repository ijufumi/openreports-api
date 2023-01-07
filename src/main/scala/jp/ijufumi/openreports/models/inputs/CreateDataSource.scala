package jp.ijufumi.openreports.models.inputs

case class CreateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
