package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  DriverType => DriverTypeEntity,
}
import jp.ijufumi.openreports.presentation.models.responses.{DriverType => DriverTypeResponse}
import scala.language.implicitConversions

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
) {
  implicit def toResponse: DriverTypeResponse = {
    DriverTypeResponse(this)
  }
}

object DriverType {
  def apply(entity: DriverTypeEntity): DriverType = {
    DriverType(
      entity.id,
      entity.name,
      entity.jdbcDriverClass,
    )
  }
}
