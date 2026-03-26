package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{DriverType => DriverTypeModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{DriverType => DriverTypeEntity}

object DriverTypeConverter {
  def toDomain(entity: DriverTypeEntity): DriverTypeModel = {
    DriverTypeModel(
      entity.id,
      entity.name,
      entity.jdbcDriverClass,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def fromDriverTypeEntity(entity: DriverTypeEntity): DriverTypeModel = toDomain(entity)
    implicit def fromDriverTypeEntities(entity: Seq[DriverTypeEntity]): Seq[DriverTypeModel] =
      entity.map(toDomain)
  }
}
