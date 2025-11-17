package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.presentation.response.{DriverType => DriverTypeResponse}

trait DriverTypeUseCase {
  def getAll: Seq[DriverTypeResponse]
}
