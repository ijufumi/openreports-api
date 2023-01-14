package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.gateways.datastores.database.repositories.RoleRepository
import jp.ijufumi.openreports.models.outputs.Role
import jp.ijufumi.openreports.services.RoleService

class RoleServiceImpl @Inject()(roleRepository: RoleRepository)
    extends RoleService {
  override def getRoles: Seq[Role] = {
    roleRepository.getAll.map(v => Role(v))
  }
}
