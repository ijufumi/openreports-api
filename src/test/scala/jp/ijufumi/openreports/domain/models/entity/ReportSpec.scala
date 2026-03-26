package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ReportSpec extends AnyFlatSpec with Matchers {

  "Report" should "be creatable with valid fields" in {
    val report = Report(
      id = IDs.ulid(),
      name = "Test Report",
      templateId = "template-id",
      dataSourceId = Some("datasource-id"),
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    report should not be null
    report.name should equal("Test Report")
  }

  it should "maintain immutability" in {
    val report1 = Report(
      id = "id-1",
      name = "Original Name",
      templateId = "template-id",
      dataSourceId = None,
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val report2 = report1.copy(name = "Updated Name")

    report1.name should equal("Original Name")
    report2.name should equal("Updated Name")
    report1.id should equal(report2.id)
  }

  it should "handle None for dataSourceId" in {
    val report = Report(
      id = IDs.ulid(),
      name = "Test Report",
      templateId = "template-id",
      dataSourceId = None,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    report.dataSourceId should be(None)
  }

  it should "handle Some for dataSourceId" in {
    val report = Report(
      id = IDs.ulid(),
      name = "Test Report",
      templateId = "template-id",
      dataSourceId = Some("datasource-id"),
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    report.dataSourceId should be(defined)
    report.dataSourceId.get should equal("datasource-id")
  }

  it should "include reportTemplate when provided" in {
    val template = ReportTemplate(
      id = "template-id",
      name = "Test Template",
      filePath = "/path/to/template.xlsx",
      workspaceId = "workspace-id",
      storageType = StorageTypes.Local,
      fileSize = 1024L,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    val report = Report(
      id = IDs.ulid(),
      name = "Test Report",
      templateId = "template-id",
      dataSourceId = Some("datasource-id"),
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
      reportTemplate = Some(template),
    )

    report.reportTemplate should be(defined)
    report.reportTemplate.get.id should equal("template-id")
  }

  it should "handle special characters in name" in {
    val specialName = "Report's \"Special\" Name! @#$%"
    val report = Report(
      id = IDs.ulid(),
      name = specialName,
      templateId = "template-id",
      dataSourceId = None,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    report.name should equal(specialName)
  }

  it should "handle unicode characters in name" in {
    val unicodeName = "レポート 日本語テスト"
    val report = Report(
      id = IDs.ulid(),
      name = unicodeName,
      templateId = "template-id",
      dataSourceId = None,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    report.name should equal(unicodeName)
  }

  it should "handle empty name" in {
    val report = Report(
      id = IDs.ulid(),
      name = "",
      templateId = "template-id",
      dataSourceId = None,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    report.name should equal("")
  }

  it should "handle very long names" in {
    val longName = "A" * 500
    val report = Report(
      id = IDs.ulid(),
      name = longName,
      templateId = "template-id",
      dataSourceId = None,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    report.name should have length 500
  }

  it should "preserve timestamps" in {
    val createdAt = System.currentTimeMillis()
    val updatedAt = createdAt + 1000

    val report = Report(
      id = IDs.ulid(),
      name = "Test Report",
      templateId = "template-id",
      dataSourceId = None,
      workspaceId = "workspace-id",
      createdAt = createdAt,
      updatedAt = updatedAt,
    )

    report.createdAt should equal(createdAt)
    report.updatedAt should equal(updatedAt)
    report.updatedAt should be > report.createdAt
  }

  it should "support copy with modifications" in {
    val original = Report(
      id = "report-id",
      name = "Original Name",
      templateId = "template-id-1",
      dataSourceId = Some("datasource-1"),
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val modified = original.copy(
      name = "Modified Name",
      templateId = "template-id-2",
      dataSourceId = Some("datasource-2"),
      updatedAt = 2000L,
    )

    modified.id should equal(original.id)
    modified.name should equal("Modified Name")
    modified.templateId should equal("template-id-2")
    modified.dataSourceId should equal(Some("datasource-2"))
    modified.updatedAt should equal(2000L)
    modified.createdAt should equal(original.createdAt)
  }

  "Report equality" should "compare by value" in {
    val report1 = Report(
      id = "same-id",
      name = "Test",
      templateId = "template",
      dataSourceId = None,
      workspaceId = "workspace",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val report2 = Report(
      id = "same-id",
      name = "Test",
      templateId = "template",
      dataSourceId = None,
      workspaceId = "workspace",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    report1 should equal(report2)
  }

  it should "differentiate when values differ" in {
    val report1 = Report(
      id = "id-1",
      name = "Test",
      templateId = "template",
      dataSourceId = None,
      workspaceId = "workspace",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val report2 = Report(
      id = "id-2",
      name = "Test",
      templateId = "template",
      dataSourceId = None,
      workspaceId = "workspace",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    report1 should not equal report2
  }
}
