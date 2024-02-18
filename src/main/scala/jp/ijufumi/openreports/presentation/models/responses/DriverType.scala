package jp.ijufumi.openreports.presentation.models.responses

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{DriverType => DriverTypeEntity}

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
