package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.IDs
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WorkspaceSpec extends AnyFlatSpec with Matchers {

  "Workspace" should "be creatable with valid fields" in {
    val workspace = Workspace(
      id = IDs.ulid(),
      name = "Test Workspace",
      slug = "test-workspace",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    workspace should not be null
    workspace.name should equal("Test Workspace")
    workspace.slug should equal("test-workspace")
  }

  it should "maintain immutability" in {
    val workspace1 = Workspace(
      id = "workspace-id",
      name = "Original Name",
      slug = "original-slug",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val workspace2 = workspace1.copy(name = "Updated Name")

    workspace1.name should equal("Original Name")
    workspace2.name should equal("Updated Name")
    workspace1.id should equal(workspace2.id)
  }

  it should "handle slug with lowercase and hyphens" in {
    val slugs = Seq(
      "simple-slug",
      "workspace-123",
      "my-team-workspace",
      "test",
      "a-b-c-d-e-f",
    )

    slugs.foreach { slug =>
      val workspace = Workspace(
        id = IDs.ulid(),
        name = "Test",
        slug = slug,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
      )

      workspace.slug should equal(slug)
    }
  }

  it should "handle special characters in name" in {
    val specialName = "Workspace's \"Special\" Name! @#$%"
    val workspace = Workspace(
      id = IDs.ulid(),
      name = specialName,
      slug = "special-workspace",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    workspace.name should equal(specialName)
  }

  it should "handle unicode characters in name" in {
    val unicodeName = "ワークスペース 日本語"
    val workspace = Workspace(
      id = IDs.ulid(),
      name = unicodeName,
      slug = "japanese-workspace",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    workspace.name should equal(unicodeName)
  }

  it should "handle empty name" in {
    val workspace = Workspace(
      id = IDs.ulid(),
      name = "",
      slug = "empty-name",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    workspace.name should equal("")
  }

  it should "handle very long names" in {
    val longName = "A" * 500
    val workspace = Workspace(
      id = IDs.ulid(),
      name = longName,
      slug = "long-name-workspace",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    workspace.name should have length 500
  }

  it should "preserve timestamps" in {
    val createdAt = System.currentTimeMillis()
    val updatedAt = createdAt + 1000

    val workspace = Workspace(
      id = IDs.ulid(),
      name = "Test Workspace",
      slug = "test-workspace",
      createdAt = createdAt,
      updatedAt = updatedAt,
    )

    workspace.createdAt should equal(createdAt)
    workspace.updatedAt should equal(updatedAt)
    workspace.updatedAt should be > workspace.createdAt
  }

  it should "support copy with modifications" in {
    val original = Workspace(
      id = "workspace-id",
      name = "Original Name",
      slug = "original-slug",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val modified = original.copy(
      name = "Modified Name",
      slug = "modified-slug",
      updatedAt = 2000L,
    )

    modified.id should equal(original.id)
    modified.name should equal("Modified Name")
    modified.slug should equal("modified-slug")
    modified.updatedAt should equal(2000L)
    modified.createdAt should equal(original.createdAt)
  }

  it should "preserve version numbers" in {
    val workspace = Workspace(
      id = IDs.ulid(),
      name = "Test Workspace",
      slug = "test-workspace",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
      versions = 5,
    )

    workspace.versions should equal(5)
  }

  "Workspace equality" should "compare by value" in {
    val workspace1 = Workspace(
      id = "same-id",
      name = "Test",
      slug = "test",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val workspace2 = Workspace(
      id = "same-id",
      name = "Test",
      slug = "test",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    workspace1 should equal(workspace2)
  }

  it should "differentiate when values differ" in {
    val workspace1 = Workspace(
      id = "id-1",
      name = "Test",
      slug = "test",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val workspace2 = Workspace(
      id = "id-2",
      name = "Test",
      slug = "test",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    workspace1 should not equal workspace2
  }

  it should "differentiate when slug differs" in {
    val workspace1 = Workspace(
      id = "same-id",
      name = "Test",
      slug = "slug-1",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val workspace2 = Workspace(
      id = "same-id",
      name = "Test",
      slug = "slug-2",
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    workspace1 should not equal workspace2
  }

  "Workspace slug" should "support URL-friendly formats" in {
    val workspace = Workspace(
      id = IDs.ulid(),
      name = "My Team Workspace",
      slug = "my-team-workspace",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    workspace.slug should fullyMatch regex "[a-z0-9-]+"
  }

  it should "handle numeric slugs" in {
    val workspace = Workspace(
      id = IDs.ulid(),
      name = "Workspace 123",
      slug = "workspace-123",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    workspace.slug should equal("workspace-123")
  }
}
