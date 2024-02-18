package jp.ijufumi.openreports.presentation.models.responses

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{Function => FunctionEntity}

case class Function(resource: String, action: ActionType)

object Function {
  def apply(entity: FunctionEntity): Function = {
    Function(entity.resource, entity.action)
  }
}
