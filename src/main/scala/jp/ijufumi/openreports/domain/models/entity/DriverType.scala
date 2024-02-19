package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  DriverType => DriverTypeEntity,
}

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
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
