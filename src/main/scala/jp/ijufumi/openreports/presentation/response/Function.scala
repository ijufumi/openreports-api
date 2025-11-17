package jp.ijufumi.openreports.presentation.response

import jp.ijufumi.openreports.domain.models.value.enums.ActionTypes
import jp.ijufumi.openreports.infrastructure.persistence.entity.{Function => FunctionEntity}

case class Function(resource: String, action: ActionTypes.ActionType)

object Function {
  def apply(entity: FunctionEntity): Function = {
    Function(entity.resource, entity.action)
  }
}
