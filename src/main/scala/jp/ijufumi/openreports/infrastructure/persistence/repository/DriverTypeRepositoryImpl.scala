package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.DriverTypeRepository
import jp.ijufumi.openreports.domain.models.entity.DriverType
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class DriverTypeRepositoryImpl extends DriverTypeRepository {
  override def getAll(db: Database): Seq[DriverType] = {
    Await.result(db.run(driverTypeQuery.result), queryTimeout).map(v => DriverType(v))
  }
}
