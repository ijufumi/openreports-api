package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.models.outputs.Role

trait RoleService {
  def getRoles: Seq[Role]
}
