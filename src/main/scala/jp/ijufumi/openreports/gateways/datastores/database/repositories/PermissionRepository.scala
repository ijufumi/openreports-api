package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.entities.Permission

trait PermissionRepository {
  def getAll: Seq[Permission]
}
