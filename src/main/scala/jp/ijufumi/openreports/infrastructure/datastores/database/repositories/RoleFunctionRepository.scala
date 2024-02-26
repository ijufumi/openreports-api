package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.RoleFunction

trait RoleFunctionRepository {
  def getAll(
  ): Seq[RoleFunction]

  def getByRoleId(roleId: String): Seq[RoleFunction]

  def getByFunctionId(functionId: String): Seq[RoleFunction]
}
