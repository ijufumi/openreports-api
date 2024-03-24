package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Role
import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import slick.jdbc.JdbcBackend.Database

trait RoleRepository {
  def getAll(db: Database): Seq[Role]

  def getByType(db: Database, roleType: RoleTypes.RoleType): Option[Role]
}
