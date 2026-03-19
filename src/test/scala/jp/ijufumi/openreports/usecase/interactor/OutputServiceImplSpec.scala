package jp.ijufumi.openreports.usecase.interactor

import _root_.jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import _root_.jp.ijufumi.openreports.domain.port.AppConfigPort
import _root_.jp.ijufumi.openreports.usecase.port.input.{DataSourceUseCase, StorageUseCase}
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar

import java.nio.file.{Files, Paths}

class OutputInteractorSpec extends AnyFlatSpec with MockitoSugar {

  "output" should "output a file" in {
    // mock
    val dataSourceService = mock[DataSourceUseCase]
    val storageService = mock[StorageUseCase]
    val appConfig = mock[AppConfigPort]
    when(appConfig.outputFilePath).thenReturn("/tmp/openreports/output")

    val outputService = new OutputInteractor(dataSourceService, storageService, appConfig)

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
