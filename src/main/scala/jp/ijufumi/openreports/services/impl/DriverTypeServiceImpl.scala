package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.services.DriverTypeService
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.DriverTypeRepository
import jp.ijufumi.openreports.presentation.models.responses.DriverType
import slick.jdbc.JdbcBackend.Database

class DriverTypeServiceImpl @Inject() (db: Database, driverTypeRepository: DriverTypeRepository)
    extends DriverTypeService {
  override def getAll: Seq[DriverType] = {
    driverTypeRepository.getAll(db).map(d => DriverType(d))
  }
}
