package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.Permission
import jp.ijufumi.openreports.entities.enums.PermissionTypes.PermissionType
import jp.ijufumi.openreports.gateways.datastores.database.repositories.PermissionRepository
import queries.{permissionQuery => query}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class PermissionRepositoryImpl @Inject() (db: Database) extends PermissionRepository {
  override def getAll: Seq[Permission] = {
    Await.result(db.run(query.result), queryTimeout)
  }

  override def getByType(permissionType: PermissionType): Option[Permission] = {
    val getByType = query.filter(_.name === permissionType.toString)
    val result = Await.result(db.run(getByType.result), queryTimeout)
    if (result.isEmpty) {
      return None
    }
    Some(result.head)
  }
}
