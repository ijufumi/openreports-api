package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{Function => FunctionEntity}
import jp.ijufumi.openreports.models.value.enums.ActionTypes.ActionType

case class Function(resource: String, action: ActionType)

object Function {
  def apply(entity: FunctionEntity): Function = {
    Function(entity.resource, entity.action)
  }
}
