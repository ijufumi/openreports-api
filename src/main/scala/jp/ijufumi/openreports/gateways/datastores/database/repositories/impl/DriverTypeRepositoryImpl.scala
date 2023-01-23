package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.DriverType
import jp.ijufumi.openreports.gateways.datastores.database.repositories.DriverTypeRepository
import queries.{driverTypeQuery => query}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class DriverTypeRepositoryImpl  @Inject() (db: Database) extends DriverTypeRepository {
  override def getAll: Seq[DriverType] = {
    Await.result(db.run(query.result), queryTimeout)
  }
}
