package jp.ijufumi.openreports.presentation.converter

import jp.ijufumi.openreports.domain.models.entity.{Function => FunctionModel}
import jp.ijufumi.openreports.presentation.response.{Function => FunctionResponse}

object FunctionConverter {
  def toResponse(model: FunctionModel): FunctionResponse = {
    FunctionResponse(model.resource, model.action)
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toFunctionResponse(model: FunctionModel): FunctionResponse = toResponse(model)
    implicit def toFunctionResponses(model: Seq[FunctionModel]): Seq[FunctionResponse] =
      model.map(toResponse)
  }
}
