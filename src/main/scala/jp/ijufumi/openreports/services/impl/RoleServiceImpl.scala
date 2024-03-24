package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.RoleRepository
import jp.ijufumi.openreports.presentation.models.responses.Role
import jp.ijufumi.openreports.domain.models.entity.Role.conversions._
import jp.ijufumi.openreports.services.RoleService
import slick.jdbc.JdbcBackend.Database

class RoleServiceImpl @Inject() (db: Database, roleRepository: RoleRepository) extends RoleService {
  override def getRoles: Seq[Role] = {
    roleRepository.getAll(db)
  }
}
