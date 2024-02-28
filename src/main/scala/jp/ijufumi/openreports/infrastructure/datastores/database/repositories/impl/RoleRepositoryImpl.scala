package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.domain.models.entity.Role
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.RoleRepository
import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.roleTypeMapper
import queries.{roleQuery => query}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class RoleRepositoryImpl @Inject() (db: Database) extends RoleRepository {
  override def getAll: Seq[Role] = {
    Await.result(db.run(query.result), queryTimeout).map(r => Role(r))
  }

  override def getByType(roleType: RoleTypes.RoleType): Option[Role] = {
    val getByType = query.filter(_.roleType === roleType)
    val result = Await.result(db.run(getByType.result), queryTimeout)
    if (result.isEmpty) {
      return None
    }
    Some(result.head).map(r => Role(r))
  }
}
