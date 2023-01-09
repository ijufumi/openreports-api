package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.Permission
import jp.ijufumi.openreports.gateways.datastores.database.repositories.PermissionRepository
import queries.{permissionQuery => query}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class PermissionRepositoryImpl @Inject() (db: Database) extends PermissionRepository {
  override def getAll: Seq[Permission] = {
    Await.result(db.run(query.result), queryTimeout)
  }
}
