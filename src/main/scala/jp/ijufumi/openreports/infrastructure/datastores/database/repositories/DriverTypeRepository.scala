package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.DriverType

trait DriverTypeRepository {
  def getAll: Seq[DriverType]
}
