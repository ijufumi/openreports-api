package jp.ijufumi.openreports.usecase.port.input

import jp.ijufumi.openreports.presentation.responses.{DriverType => DriverTypeResponse}

trait DriverTypeUseCase {
  def getAll: Seq[DriverTypeResponse]
}
