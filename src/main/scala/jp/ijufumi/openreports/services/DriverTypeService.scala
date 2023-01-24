package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.models.outputs.{DriverType => DriverTypeResponse}

trait DriverTypeService {
  def getAll: Seq[DriverTypeResponse]
}
