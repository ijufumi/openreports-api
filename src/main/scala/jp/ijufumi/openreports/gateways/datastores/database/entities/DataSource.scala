package jp.ijufumi.openreports.gateways.datastores.database.entities

import jp.ijufumi.openreports.models.inputs.UpdateDataSource
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class DataSource(
    id: String,
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def copyForUpdate(input: UpdateDataSource): DataSource = {
    this.copy(name = input.name)
  }
}

class DataSources(tag: Tag)
    extends EntityBase[DataSource](
      tag,
      "data_sources",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def url = column[String]("url")
  def username = column[String]("username")
  def password = column[String]("password")
  def driverTypeId = column[String]("driver_type_id")
  def workspaceId = column[String]("workspace_id")

  override def * =
    (
      id,
      name,
      url,
      username,
      password,
      driverTypeId,
      workspaceId,
      createdAt,
      updatedAt,
      versions,
    ) <> (DataSource.tupled, DataSource.unapply)
}
