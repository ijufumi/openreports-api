package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.models.outputs.RoleFunction

trait RoleFunctionRepository {
  def getAll(
  ): Seq[RoleFunction]

  def getByRoleId(roleId: String): Seq[RoleFunction]

  def getByFunctionId(functionId: String): Seq[RoleFunction]
}
