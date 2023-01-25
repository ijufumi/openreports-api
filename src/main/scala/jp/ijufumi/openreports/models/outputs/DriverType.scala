package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.entities.enums.JdbcDriverClasses.JdbcDriverClass
import jp.ijufumi.openreports.entities.{DriverType => DriverTypeEntity}

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: JdbcDriverClass,
)

object DriverType {
  def apply(entity: DriverTypeEntity): DriverType = {
    DriverType(
      entity.id,
      entity.name,
      entity.jdbcDriverClass,
    )
  }
}
