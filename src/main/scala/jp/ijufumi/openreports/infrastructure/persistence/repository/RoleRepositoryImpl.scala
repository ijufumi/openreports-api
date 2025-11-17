package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.Role
import jp.ijufumi.openreports.domain.models.entity.Role.conversions._
import jp.ijufumi.openreports.domain.repository.RoleRepository
import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import jp.ijufumi.openreports.infrastructure.persistence.entity.roleTypeMapper
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class RoleRepositoryImpl extends RoleRepository {
  override def getAll(db: Database): Seq[Role] = {
    val result = Await.result(db.run(roleQuery.result), queryTimeout)
    result
  }

  override def getByType(db: Database, roleType: RoleTypes.RoleType): Option[Role] = {
    val getByType = roleQuery.filter(_.roleType === roleType)
    val result = Await.result(db.run(getByType.result), queryTimeout)
    if (result.isEmpty) {
      return None
    }
    Some(result.head)
  }
}
