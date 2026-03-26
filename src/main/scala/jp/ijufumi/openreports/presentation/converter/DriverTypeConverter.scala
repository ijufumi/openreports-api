package jp.ijufumi.openreports.presentation.converter

import jp.ijufumi.openreports.domain.models.entity.{DriverType => DriverTypeModel}
import jp.ijufumi.openreports.presentation.response.{DriverType => DriverTypeResponse}

object DriverTypeConverter {
  def toResponse(model: DriverTypeModel): DriverTypeResponse = {
    DriverTypeResponse(model.id, model.name, model.jdbcDriverClass)
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toDriverTypeResponse(model: DriverTypeModel): DriverTypeResponse = toResponse(
      model,
    )
    implicit def toDriverTypeResponses(model: Seq[DriverTypeModel]): Seq[DriverTypeResponse] =
      model.map(toResponse)
  }
}
