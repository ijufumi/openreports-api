package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.services.DriverTypeService
import jp.ijufumi.openreports.gateways.datastores.database.repositories.DriverTypeRepository
import jp.ijufumi.openreports.interfaces.models.outputs.DriverType

class DriverTypeServiceImpl @Inject() (driverTypeRepository: DriverTypeRepository)
    extends DriverTypeService {
  override def getAll: Seq[DriverType] = {
    driverTypeRepository.getAll
  }
}
