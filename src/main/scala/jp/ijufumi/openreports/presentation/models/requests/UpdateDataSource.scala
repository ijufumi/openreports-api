package jp.ijufumi.openreports.presentation.models.requests

case class UpdateDataSource(
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
)
