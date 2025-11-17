package jp.ijufumi.openreports.usecase.interactor

import com.google.inject.Inject
import jp.ijufumi.openreports.domain.repository.RoleRepository
import jp.ijufumi.openreports.presentation.responses.Role
import jp.ijufumi.openreports.domain.models.entity.Role.conversions._
import jp.ijufumi.openreports.usecase.port.input.RoleUseCase
import slick.jdbc.JdbcBackend.Database

class RoleInteractor @Inject() (db: Database, roleRepository: RoleRepository) extends RoleUseCase {
  override def getRoles: Seq[Role] = {
    roleRepository.getAll(db)
  }
}
