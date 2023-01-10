package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.gateways.datastores.database.repositories.PermissionRepository
import jp.ijufumi.openreports.models.outputs.Permission
import jp.ijufumi.openreports.services.PermissionService

class PermissionServiceImpl @Inject() (permissionRepository: PermissionRepository)
    extends PermissionService {
  override def getPermissions: Seq[Permission] = {
    permissionRepository.getAll.map(v => Permission(v))
  }
}
