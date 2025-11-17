package jp.ijufumi.openreports.usecase.interactor

import com.google.inject.Inject
import jp.ijufumi.openreports.usecase.port.input.DriverTypeUseCase
import jp.ijufumi.openreports.domain.repository.DriverTypeRepository
import jp.ijufumi.openreports.presentation.response.DriverType
import slick.jdbc.JdbcBackend.Database

class DriverTypeInteractor @Inject() (db: Database, driverTypeRepository: DriverTypeRepository)
    extends DriverTypeUseCase {
  override def getAll: Seq[DriverType] = {
    driverTypeRepository.getAll(db).map(d => DriverType(d))
  }
}
