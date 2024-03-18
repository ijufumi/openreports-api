package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.DriverTypeRepository
import jp.ijufumi.openreports.domain.models.entity.DriverType
import queries.{driverTypeQuery => query}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class DriverTypeRepositoryImpl extends DriverTypeRepository {
  override def getAll(db: Database): Seq[DriverType] = {
    Await.result(db.run(query.result), queryTimeout).map(v => DriverType(v))
  }
}
