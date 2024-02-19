package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.presentation.models.responses.{DriverType => DriverTypeResponse}

trait DriverTypeService {
  def getAll: Seq[DriverTypeResponse]
}
