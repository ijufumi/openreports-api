package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.interfaces.models.outputs.Role

trait RoleService {
  def getRoles: Seq[Role]
}
