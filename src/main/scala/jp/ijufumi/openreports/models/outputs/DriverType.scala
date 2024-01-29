package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.models.value.enums.JdbcDriverClasses.JdbcDriverClass
import jp.ijufumi.openreports.gateways.datastores.database.entities.{DriverType => DriverTypeEntity}

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
