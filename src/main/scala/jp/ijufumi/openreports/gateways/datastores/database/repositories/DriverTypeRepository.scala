package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.gateways.datastores.database.entities.DriverType

trait DriverTypeRepository {
  def getAll: Seq[DriverType]
}
