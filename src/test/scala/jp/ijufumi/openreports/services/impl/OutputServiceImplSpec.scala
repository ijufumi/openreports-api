package jp.ijufumi.openreports.services.impl

import _root_.jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import _root_.jp.ijufumi.openreports.services.{DataSourceService, StorageService}
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar

import java.nio.file.{Files, Paths}

class OutputServiceImplSpec extends AnyFlatSpec with MockitoSugar {

  "output" should "output a file" in {
    // mock
    val dataSourceService = mock[DataSourceService]
    val storageService = mock[StorageService]

    val outputService = new OutputServiceImpl(dataSourceService, storageService)

    val workspaceId = "1"
    val filePath = "test.xlsx"
    val storageType = StorageTypes.Local
    val dataSourceId = None
    val asPDF = false

    val inputFile = Paths.get("src/test/resources/test.xlsx")

    when(storageService.get(workspaceId, filePath, storageType)).thenReturn(inputFile)

    // when
    val actual = outputService.output(workspaceId, filePath, storageType, dataSourceId, asPDF)

    // then
    assert(actual.isDefined)
    assert(actual.get.exists())
  }
}
