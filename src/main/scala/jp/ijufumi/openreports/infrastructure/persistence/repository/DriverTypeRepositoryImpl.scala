package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.DriverTypeRepository
import jp.ijufumi.openreports.domain.models.entity.DriverType
import jp.ijufumi.openreports.infrastructure.persistence.converter.DriverTypeConverter
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class DriverTypeRepositoryImpl extends DriverTypeRepository {
  override def getAll(db: Database): Seq[DriverType] = {
    Await
      .result(db.run(driverTypeQuery.result), queryTimeout)
      .map(v => DriverTypeConverter.toDomain(v))
  }
}
