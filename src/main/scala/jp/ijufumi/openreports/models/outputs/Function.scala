package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{Function => FunctionEntity}

case class Function(resource: String, action: String)

object Function {
  def apply(entity: FunctionEntity): Function = {
    Function(entity.resource, entity.action)
  }
}
