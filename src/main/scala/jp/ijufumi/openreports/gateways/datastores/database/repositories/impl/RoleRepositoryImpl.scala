package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.gateways.datastores.database.entities._
import jp.ijufumi.openreports.models.value.enums.RoleTypes.RoleType
import jp.ijufumi.openreports.gateways.datastores.database.repositories.RoleRepository
import queries.{roleQuery => query}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class RoleRepositoryImpl @Inject()(db: Database) extends RoleRepository {
  override def getAll: Seq[Role] = {
    Await.result(db.run(query.result), queryTimeout)
  }

  override def getByType(roleType: RoleType): Option[Role] = {
    val getByType = query.filter(_.roleType === roleType)
    val result = Await.result(db.run(getByType.result), queryTimeout)
    if (result.isEmpty) {
      return None
    }
    Some(result.head)
  }
}
