package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.interfaces.models.outputs.DriverType

trait DriverTypeRepository {
  def getAll: Seq[DriverType]
}
