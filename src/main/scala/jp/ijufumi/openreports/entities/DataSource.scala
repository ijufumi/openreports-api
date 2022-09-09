package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime

case class DataSource(
    id: String,
    name: String,
    url: String,
    driverTypeId: String,
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
  def driver_type_id = column[String]("driver_type_id")
  def createdAt = column[Timestamp]("created_at")
  def updatedAt = column[Timestamp]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      name,
      url,
      driver_type_id,
      createdAt,
      updatedAt,
      versions,
    ) <> (DataSource.tupled, DataSource.unapply)
}