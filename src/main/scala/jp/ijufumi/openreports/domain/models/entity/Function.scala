package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.ActionTypes
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  Function => FunctionEntity,
}
import jp.ijufumi.openreports.presentation.models.responses.{Function => FunctionResponse}
import jp.ijufumi.openreports.utils.Dates

case class Function(
    id: String,
    resource: String,
    action: ActionTypes.ActionType,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toResponse: FunctionResponse = {
    FunctionResponse(
      this.resource,
      this.action,
    )
  }
}

object Function {
  def apply(entity: FunctionEntity): Function = {
    Function(
      entity.id,
      entity.resource,
      entity.action,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
}
