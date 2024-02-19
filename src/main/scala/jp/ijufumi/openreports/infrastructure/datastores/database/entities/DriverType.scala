package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import jp.ijufumi.openreports.utils.Dates
import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import slick.jdbc.PostgresProfile.api._

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
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
  def jdbcDriverClass = column[JdbcDriverClasses.JdbcDriverClass]("jdbc_driver_class")

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
