package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  DriverType => DriverTypeEntity,
}
import jp.ijufumi.openreports.presentation.response.{DriverType => DriverTypeResponse}

case class DriverType(
    id: String,
    name: String,
    jdbcDriverClass: JdbcDriverClasses.JdbcDriverClass,
) {
  def toResponse: DriverTypeResponse = {
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

  object conversions {
    import scala.language.implicitConversions

    implicit def toResponse(model: DriverType): DriverTypeResponse = {
      model.toResponse
    }
    implicit def toResponse2(model: Option[DriverType]): Option[DriverTypeResponse] = {
      model.map(d => d.toResponse)
    }
  }
}
