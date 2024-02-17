package jp.ijufumi.openreports.interfaces.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{Function => FunctionEntity}
import jp.ijufumi.openreports.interfaces.models.value.enums.ActionTypes.ActionType

case class Function(resource: String, action: ActionType)

object Function {
  def apply(entity: FunctionEntity): Function = {
    Function(entity.resource, entity.action)
  }
}
