package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime

case class DataSource(
    id: String,
    name: String,
    url: String,
    username: String,
    password: String,
    driverTypeId: String,
    workspaceId: String,
    createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    versions: Long = 1,
)

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
  def workspaceId = column[String]("workspaceId")
  def createdAt = column[Timestamp]("created_at")
  def updatedAt = column[Timestamp]("updated_at")
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
