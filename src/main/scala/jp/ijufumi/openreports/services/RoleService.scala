package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.presentation.models.responses.Role

trait RoleService {
  def getRoles: Seq[Role]
}
