package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.domain.models.entity.{DriverType => DriverTypeModel}

trait DriverTypeUseCase {
  def getAll: Seq[DriverTypeModel]
}
