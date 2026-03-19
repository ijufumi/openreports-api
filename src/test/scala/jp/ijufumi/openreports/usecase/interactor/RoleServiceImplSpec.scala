package jp.ijufumi.openreports.usecase.interactor

import _root_.jp.ijufumi.openreports.domain.models.entity.{Role => RoleModel}
import _root_.jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import _root_.jp.ijufumi.openreports.domain.repository.RoleRepository
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class RoleInteractorSpec extends AnyFlatSpec with MockitoSugar {

  "getRoles" should "return all roles" in {
    // mock
    val db = mock[Database]
    val roleRepository = mock[RoleRepository]

    val roleService = new RoleInteractor(db, roleRepository)

    val role = RoleModel("1", RoleTypes.Admin)

    when(roleRepository.getAll(db)).thenReturn(Seq(role))

    // when
    val actual = roleService.getRoles

    // then
    assert(actual.isInstanceOf[Seq[RoleModel]])
    assert(actual.length == 1)
    assert(actual.head.id == "1")
  }
}