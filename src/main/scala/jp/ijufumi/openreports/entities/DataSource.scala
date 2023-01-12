package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.models.inputs.{CreateDataSource, UpdateDataSource}
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

object DataSource {
  def apply(id: String, workspaceId: String, input: CreateDataSource): DataSource = {
    DataSource(
      id,
      input.name,
      input.url,
      input.username,
      input.password,
      input.driverTypeId,
      workspaceId,
    )
  }
}

class DataSources(tag: Tag)
    extends Table[DataSource](
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
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

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
