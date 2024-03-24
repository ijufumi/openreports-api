package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.RoleFunction
import slick.jdbc.JdbcBackend.Database

trait RoleFunctionRepository {
  def getAll(
      db: Database,
  ): Seq[RoleFunction]

  def getByRoleId(db: Database,roleId: String): Seq[RoleFunction]

  def getByFunctionId(db: Database,functionId: String): Seq[RoleFunction]
}
