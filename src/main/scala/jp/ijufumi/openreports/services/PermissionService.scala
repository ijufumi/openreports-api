package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.models.outputs.Permission

trait PermissionService {
  def getPermissions: Seq[Permission]
}
