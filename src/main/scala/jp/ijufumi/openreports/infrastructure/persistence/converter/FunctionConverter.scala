package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{Function => FunctionModel}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Function => FunctionEntity}

object FunctionConverter {
  def toDomain(entity: FunctionEntity): FunctionModel = {
    FunctionModel(
      entity.id,
      entity.resource,
      entity.action,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def fromFunctionEntity(entity: FunctionEntity): FunctionModel = toDomain(entity)
    implicit def fromFunctionEntity2(entity: Option[FunctionEntity]): Option[FunctionModel] =
      entity.map(toDomain)
    implicit def fromFunctionEntities(entity: Seq[FunctionEntity]): Seq[FunctionModel] =
      entity.map(toDomain)
  }
}
