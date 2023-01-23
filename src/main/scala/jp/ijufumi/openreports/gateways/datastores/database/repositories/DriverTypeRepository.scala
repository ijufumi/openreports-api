package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.entities.DriverType

trait DriverTypeRepository {
  def getAll: Seq[DriverType]
}
