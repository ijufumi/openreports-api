package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.presentation.models.responses.DriverType

trait DriverTypeRepository {
  def getAll: Seq[DriverType]
}
