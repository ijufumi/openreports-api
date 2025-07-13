package jp.ijufumi.openreports.services.impl

import _root_.jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import _root_.jp.ijufumi.openreports.infrastructure.filestores.local.{LocalFileRepository, LocalSeedFileRepository}
import _root_.jp.ijufumi.openreports.infrastructure.filestores.s3.AwsS3Repository
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar

import java.nio.file.Paths

class StorageServiceImplSpec extends AnyFlatSpec with MockitoSugar {

  "get" should "get a file from local" in {
    // mock
    val localFileRepository = mock[LocalFileRepository]
    val localSeedFileRepository = mock[LocalSeedFileRepository]
    val awsS3Repository = mock[AwsS3Repository]

    val storageService = new StorageServiceImpl(localFileRepository, localSeedFileRepository, awsS3Repository)

    val workspaceId = "1"
    val key = "test.xlsx"
    val storageType = StorageTypes.Local
    val expected = Paths.get(key)

    when(localFileRepository.get(workspaceId, key)).thenReturn(expected)

    // when
    val actual = storageService.get(workspaceId, key, storageType)

    // then
    assert(actual == expected)
  }

  "create" should "create a file in local" in {
    // mock
    val localFileRepository = mock[LocalFileRepository]
    val localSeedFileRepository = mock[LocalSeedFileRepository]
    val awsS3Repository = mock[AwsS3Repository]

    val storageService = new StorageServiceImpl(localFileRepository, localSeedFileRepository, awsS3Repository)

    val workspaceId = "1"
    val key = "test.xlsx"
    val storageType = StorageTypes.Local
    val file = Paths.get(key)

    // when
    storageService.create(workspaceId, key, storageType, file)

    // then
    verify(localFileRepository, times(1)).create(workspaceId, key, file)
  }
}
