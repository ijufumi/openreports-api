package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: String,
    createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    versions: Long = 1,
)

class DriverTypes(tag: Tag)
    extends Table[DriverType](
      tag,
      "driver_types",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def jdbcDriverClass = column[String]("jdbc_driver_class")
  def createdAt = column[Timestamp]("created_at")
  def updatedAt = column[Timestamp]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      name,
      jdbcDriverClass,
      createdAt,
      updatedAt,
      versions,
    ) <> (DriverType.tupled, DriverType.unapply)
}
