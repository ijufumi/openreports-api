package jp.ijufumi.openreports.presentation.models.responses

case class DataSource(
    id: String,
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
    driverType: Option[DriverType] = None,
    createdAt: Long,
    updatedAt: Long,
)
