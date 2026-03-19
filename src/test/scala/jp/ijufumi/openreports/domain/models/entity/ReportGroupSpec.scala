package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.IDs
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ReportGroupSpec extends AnyFlatSpec with Matchers {

  "ReportGroup" should "be creatable with valid fields" in {
    val group = ReportGroup(
      id = IDs.ulid(),
      name = "Test Group",
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    group should not be null
    group.name should equal("Test Group")
    group.workspaceId should equal("workspace-id")
  }

  it should "maintain immutability" in {
    val group1 = ReportGroup(
      id = "group-id",
      name = "Original Name",
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val group2 = group1.copy(name = "Updated Name")

    group1.name should equal("Original Name")
    group2.name should equal("Updated Name")
    group1.id should equal(group2.id)
  }

  it should "handle special characters in name" in {
    val specialName = "Group's \"Special\" Name! @#$%"
    val group = ReportGroup(
      id = IDs.ulid(),
      name = specialName,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    group.name should equal(specialName)
  }

  it should "handle unicode characters in name" in {
    val unicodeName = "グループ 日本語"
    val group = ReportGroup(
      id = IDs.ulid(),
      name = unicodeName,
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    group.name should equal(unicodeName)
  }

  it should "handle empty name" in {
    val group = ReportGroup(
      id = IDs.ulid(),
      name = "",
      workspaceId = "workspace-id",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    group.name should equal("")
  }

  it should "preserve timestamps" in {
    val createdAt = System.currentTimeMillis()
    val updatedAt = createdAt + 1000

    val group = ReportGroup(
      id = IDs.ulid(),
      name = "Test Group",
      workspaceId = "workspace-id",
      createdAt = createdAt,
      updatedAt = updatedAt
    )

    group.createdAt should equal(createdAt)
    group.updatedAt should equal(updatedAt)
    group.updatedAt should be > group.createdAt
  }

  it should "support copy with modifications" in {
    val original = ReportGroup(
      id = "group-id",
      name = "Original Name",
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val modified = original.copy(
      name = "Modified Name",
      updatedAt = 2000L
    )

    modified.id should equal(original.id)
    modified.name should equal("Modified Name")
    modified.updatedAt should equal(2000L)
    modified.createdAt should equal(original.createdAt)
  }

  "ReportGroup equality" should "compare by value" in {
    val group1 = ReportGroup(
      id = "same-id",
      name = "Test",
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val group2 = ReportGroup(
      id = "same-id",
      name = "Test",
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    group1 should equal(group2)
  }

  it should "differentiate when values differ" in {
    val group1 = ReportGroup(
      id = "id-1",
      name = "Test",
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val group2 = ReportGroup(
      id = "id-2",
      name = "Test",
      workspaceId = "workspace-id",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    group1 should not equal group2
  }

  it should "differentiate when workspace differs" in {
    val group1 = ReportGroup(
      id = "same-id",
      name = "Test",
      workspaceId = "workspace-1",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val group2 = ReportGroup(
      id = "same-id",
      name = "Test",
      workspaceId = "workspace-2",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    group1 should not equal group2
  }
}
