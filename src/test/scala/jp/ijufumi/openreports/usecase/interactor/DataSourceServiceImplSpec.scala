package jp.ijufumi.openreports.usecase.interactor

import _root_.jp.ijufumi.openreports.domain.models.entity.{
  DataSource => DataSourceModel,
  DriverType => DriverTypeModel,
}
import _root_.jp.ijufumi.openreports.domain.models.value.enums.JdbcDriverClasses
import _root_.jp.ijufumi.openreports.domain.repository.DataSourceRepository
import _root_.jp.ijufumi.openreports.presentation.response.{DataSource, DriverType, Lists}
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import slick.jdbc.JdbcBackend.Database

class DataSourceInteractorSpec extends AnyFlatSpec with MockitoSugar {

  "getDataSources" should "return data sources" in {
    // mock
    val db = mock[Database]
    val dataSourceRepository = mock[DataSourceRepository]

    val dataSourceService = new DataSourceInteractor(db, dataSourceRepository)

    val workspaceId = "1"
    val driverType = DriverTypeModel("1", "test-driver", JdbcDriverClasses.Postgres)
    val dataSource = DataSourceModel(
      "1",
      "test",
      "test-url",
      "test-user",
      "test-pass",
      "1",
      0,
      "1",
      10,
      0,
      0,
      Some(driverType),
    )

    when(dataSourceRepository.getAllWithDriverType(db, workspaceId)).thenReturn(Seq(dataSource))

    // when
    val actual = dataSourceService.getDataSources(workspaceId)

    // then
    assert(actual.isInstanceOf[Lists[DataSource]])
    assert(actual.items.length == 1)
    assert(actual.count == 1)
    assert(actual.items.head.name == "test")
  }

  "getDataSource" should "return data source" in {
    // mock
    val db = mock[Database]
    val dataSourceRepository = mock[DataSourceRepository]

    val dataSourceService = new DataSourceInteractor(db, dataSourceRepository)

    val workspaceId = "1"
    val dataSourceId = "1"
    val driverType = DriverTypeModel("1", "test-driver", JdbcDriverClasses.Postgres)
    val dataSource = DataSourceModel(
      "1",
      "test",
      "test-url",
      "test-user",
      "test-pass",
      "1",
      0,
      "1",
      10,
      0,
      0,
      Some(driverType),
    )

    when(dataSourceRepository.getByIdWithDriverType(db, workspaceId, dataSourceId))
      .thenReturn(Some(dataSource))

    // when
    val actual = dataSourceService.getDataSource(workspaceId, dataSourceId)

    // then
    assert(actual.isDefined)
    assert(actual.get.name == "test")
  }
}
