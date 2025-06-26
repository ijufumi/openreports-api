package jp.ijufumi.openreports.services.impl

import _root_.jp.ijufumi.openreports.domain.models.entity.{DriverType => DriverTypeModel}
import _root_.jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import _root_.jp.ijufumi.openreports.infrastructure.datastores.database.repositories.DriverTypeRepository
import _root_.jp.ijufumi.openreports.presentation.models.responses.DriverType
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class DriverTypeServiceImplSpec extends AnyFlatSpec with MockitoSugar {

  "getAll" should "return all driver types" in {
    // mock
    val db = mock[Database]
    val driverTypeRepository = mock[DriverTypeRepository]

    val driverTypeService = new DriverTypeServiceImpl(db, driverTypeRepository)

    val driverType = DriverTypeModel("1", "test-driver", JdbcDriverClasses.Postgres)

    when(driverTypeRepository.getAll(db)).thenReturn(Seq(driverType))

    // when
    val actual = driverTypeService.getAll

    // then
    assert(actual.isInstanceOf[Seq[DriverType]])
    assert(actual.length == 1)
    assert(actual.head.name == "test-driver")
  }
}
