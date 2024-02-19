package jp.ijufumi.openreports.presentation.models.requests

case class CreateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
