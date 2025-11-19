package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.presentation.request.UpdateTemplate
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ReportTemplateSpec extends AnyFlatSpec with Matchers {

  "ReportTemplate" should "be creatable with valid fields" in {
    val template = ReportTemplate(
      id = IDs.ulid(),
      name = "Test Template",
      filePath = "/path/to/template.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    template should not be null
    template.name should equal("Test Template")
    template.filePath should equal("/path/to/template.xlsx")
  }

  it should "maintain immutability" in {
    val template1 = ReportTemplate(
      id = "template-id",
      name = "Original Name",
      filePath = "/original/path.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val template2 = template1.copy(name = "Updated Name")

    template1.name should equal("Original Name")
    template2.name should equal("Updated Name")
    template1.id should equal(template2.id)
  }

  it should "support Local storage type" in {
    val template = ReportTemplate(
      id = IDs.ulid(),
      name = "Local Template",
      filePath = "/local/path/template.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 2048L,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    template.storageType should equal(StorageTypes.Local)
  }

  it should "support S3 storage type" in {
    val template = ReportTemplate(
      id = IDs.ulid(),
      name = "S3 Template",
      filePath = "s3://bucket/path/template.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.S3,
      fileSize = 2048L,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    template.storageType should equal(StorageTypes.S3)
  }

  it should "handle various file extensions" in {
    val extensions = Seq(".xlsx", ".xls", ".pdf", ".csv", ".json")

    extensions.foreach { ext =>
      val template = ReportTemplate(
        id = IDs.ulid(),
        name = s"Template$ext",
        filePath = s"/path/to/file$ext",
        workspaceId = "workspace-id",
        storageType = StorageTypes.Local,
        fileSize = 1024L,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
      )

      template.filePath should endWith(ext)
    }
  }

  it should "handle special characters in file path" in {
    val specialPath = "/path/with spaces/file's name & special.xlsx"
    val template = ReportTemplate(
      id = IDs.ulid(),
      name = "Special Path Template",
      filePath = specialPath,
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    template.filePath should equal(specialPath)
  }

  it should "handle unicode characters in name" in {
    val unicodeName = "テンプレート 日本語"
    val template = ReportTemplate(
      id = IDs.ulid(),
      name = unicodeName,
      filePath = "/path/to/template.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    template.name should equal(unicodeName)
  }

  it should "handle zero file size" in {
    val template = ReportTemplate(
      id = IDs.ulid(),
      name = "Empty Template",
      filePath = "/path/to/empty.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 0L,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    template.fileSize should equal(0L)
  }

  it should "handle very large file sizes" in {
    val largeSize = 1024L * 1024L * 1024L // 1 GB
    val template = ReportTemplate(
      id = IDs.ulid(),
      name = "Large Template",
      filePath = "/path/to/large.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = largeSize,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    template.fileSize should equal(largeSize)
  }

  it should "preserve timestamps" in {
    val createdAt = System.currentTimeMillis()
    val updatedAt = createdAt + 1000

    val template = ReportTemplate(
      id = IDs.ulid(),
      name = "Test Template",
      filePath = "/path/to/template.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = createdAt,
      updatedAt = updatedAt
    )

    template.createdAt should equal(createdAt)
    template.updatedAt should equal(updatedAt)
    template.updatedAt should be > template.createdAt
  }

  it should "support copy with modifications" in {
    val original = ReportTemplate(
      id = "template-id",
      name = "Original Name",
      filePath = "/original/path.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val modified = original.copy(
      name = "Modified Name",
      filePath = "/modified/path.xlsx",
      storageType = StorageTypes.S3,
      fileSize = 2048L,
      updatedAt = 2000L
    )

    modified.id should equal(original.id)
    modified.name should equal("Modified Name")
    modified.filePath should equal("/modified/path.xlsx")
    modified.storageType should equal(StorageTypes.S3)
    modified.fileSize should equal(2048L)
    modified.updatedAt should equal(2000L)
    modified.createdAt should equal(original.createdAt)
  }

  "ReportTemplate copyForUpdate" should "update only name field" in {
    val original = ReportTemplate(
      id = "template-id",
      name = "Original Name",
      filePath = "/path/to/file.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = 1000L,
      updatedAt = 2000L
    )

    val updateRequest = UpdateTemplate(name = "Updated Name")
    val updated = original.copyForUpdate(updateRequest)

    updated.id should equal(original.id)
    updated.name should equal("Updated Name")
    updated.filePath should equal(original.filePath)
    updated.storageType should equal(original.storageType)
    updated.fileSize should equal(original.fileSize)
  }

  "ReportTemplate toResponse" should "include all relevant fields" in {
    val template = ReportTemplate(
      id = "template-id",
      name = "Test Template",
      filePath = "/path/to/template.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.S3,
      fileSize = 2048L,
      createdAt = 1000L,
      updatedAt = 2000L
    )

    val response = template.toResponse

    response.id should equal("template-id")
    response.name should equal("Test Template")
    response.filePath should equal("/path/to/template.xlsx")
    response.storageType should equal(StorageTypes.S3)
    response.fileSize should equal(2048L)
  }

  "ReportTemplate equality" should "compare by value" in {
    val template1 = ReportTemplate(
      id = "same-id",
      name = "Test",
      filePath = "/path/file.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val template2 = ReportTemplate(
      id = "same-id",
      name = "Test",
      filePath = "/path/file.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = 1000L,
      updatedAt = 1000L
    )

    template1 should equal(template2)
  }

  it should "differentiate when values differ" in {
    val template1 = ReportTemplate(
      id = "id-1",
      name = "Test",
      filePath = "/path/file.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val template2 = ReportTemplate(
      id = "id-2",
      name = "Test",
      filePath = "/path/file.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = 1000L,
      updatedAt = 1000L
    )

    template1 should not equal template2
  }

  it should "differentiate when storage type differs" in {
    val template1 = ReportTemplate(
      id = "same-id",
      name = "Test",
      filePath = "/path/file.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val template2 = ReportTemplate(
      id = "same-id",
      name = "Test",
      filePath = "/path/file.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.S3,
      fileSize = 1024L,
      createdAt = 1000L,
      updatedAt = 1000L
    )

    template1 should not equal template2
  }
}
