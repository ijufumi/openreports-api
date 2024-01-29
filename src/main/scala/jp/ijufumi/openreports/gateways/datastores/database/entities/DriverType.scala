package jp.ijufumi.openreports.gateways.datastores.database.entities

import jp.ijufumi.openreports.models.value.enums.JdbcDriverClasses.JdbcDriverClass
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: JdbcDriverClass,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class DriverTypes(tag: Tag)
    extends EntityBase[DriverType](
      tag,
      "driver_types",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def jdbcDriverClass = column[JdbcDriverClass]("jdbc_driver_class")

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
