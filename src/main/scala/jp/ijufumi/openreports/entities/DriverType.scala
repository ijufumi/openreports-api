package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
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
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
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
