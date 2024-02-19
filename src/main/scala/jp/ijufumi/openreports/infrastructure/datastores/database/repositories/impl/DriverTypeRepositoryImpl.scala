package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.DriverTypeRepository
import jp.ijufumi.openreports.domain.models.entity.DriverType
import queries.{driverTypeQuery => query}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class DriverTypeRepositoryImpl @Inject() (db: Database) extends DriverTypeRepository {
  override def getAll: Seq[DriverType] = {
    Await.result(db.run(query.result), queryTimeout).map(v => DriverType(v))
  }
}
