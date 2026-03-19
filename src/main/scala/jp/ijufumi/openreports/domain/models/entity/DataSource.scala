package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.Dates

case class DataSource(
    id: String,
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
    maxPoolSize: Integer,
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    driverType: Option[DriverType] = None,
)

object DataSource {
  // TODO: need to have value in db
  val maxPoolSize: Integer = 10
}
