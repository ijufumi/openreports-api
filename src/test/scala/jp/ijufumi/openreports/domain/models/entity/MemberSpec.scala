package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.IDs
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MemberSpec extends AnyFlatSpec with Matchers {

  "Member" should "be creatable with valid fields" in {
    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "hashed-password",
      name = "Test User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    member should not be null
    member.email should equal("test@example.com")
    member.name should equal("Test User")
  }

  it should "maintain immutability" in {
    val member1 = Member(
      id = "member-id",
      googleId = None,
      email = "original@example.com",
      password = "password",
      name = "Original Name",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val member2 = member1.copy(name = "Updated Name")

    member1.name should equal("Original Name")
    member2.name should equal("Updated Name")
    member1.id should equal(member2.id)
  }

  it should "handle None for googleId" in {
    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    member.googleId should be(None)
  }

  it should "handle Some for googleId" in {
    val member = Member(
      id = IDs.ulid(),
      googleId = Some("google-oauth-id-123"),
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    member.googleId should be(defined)
    member.googleId.get should equal("google-oauth-id-123")
  }

  it should "handle empty workspaces by default" in {
    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    member.workspaces should be(empty)
  }

  it should "include workspaces when provided" in {
    val workspace = Workspace(
      id = IDs.ulid(),
      name = "Test Workspace",
      slug = "slug",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
      workspaces = Seq(workspace)
    )

    member.workspaces should have size 1
    member.workspaces.head.id should equal(workspace.id)
  }

  it should "handle multiple workspaces" in {
    val workspace1 = Workspace(
      id = IDs.ulid(),
      name = "Workspace 1",
      slug = "slug",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )
    val workspace2 = Workspace(
      id = IDs.ulid(),
      name = "Workspace 2",
      slug = "slug",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
      workspaces = Seq(workspace1, workspace2)
    )

    member.workspaces should have size 2
  }

  it should "handle various email formats" in {
    val emails = Seq(
      "simple@example.com",
      "user.name@example.com",
      "user+tag@example.co.jp",
      "user_name@sub.example.com",
      "123@example.com"
    )

    emails.foreach { email =>
      val member = Member(
        id = IDs.ulid(),
        googleId = None,
        email = email,
        password = "password",
        name = "Test User",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
      )

      member.email should equal(email)
    }
  }

  it should "handle special characters in name" in {
    val specialName = "User's \"Special\" Name! @#$%"
    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = specialName,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    member.name should equal(specialName)
  }

  it should "handle unicode characters in name" in {
    val unicodeName = "山田 太郎"
    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = unicodeName,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    member.name should equal(unicodeName)
  }

  it should "handle empty name" in {
    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    member.name should equal("")
  }

  it should "handle very long names" in {
    val longName = "A" * 500
    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = longName,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    member.name should have length 500
  }

  it should "preserve hashed password" in {
    val hashedPassword = "hashed-password-with-salt-xyz123"
    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = hashedPassword,
      name = "Test User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    member.password should equal(hashedPassword)
  }

  it should "preserve timestamps" in {
    val createdAt = System.currentTimeMillis()
    val updatedAt = createdAt + 1000

    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = createdAt,
      updatedAt = updatedAt
    )

    member.createdAt should equal(createdAt)
    member.updatedAt should equal(updatedAt)
    member.updatedAt should be > member.createdAt
  }

  it should "support copy with modifications" in {
    val original = Member(
      id = "member-id",
      googleId = None,
      email = "old@example.com",
      password = "old-password",
      name = "Old Name",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val modified = original.copy(
      email = "new@example.com",
      name = "New Name",
      updatedAt = 2000L
    )

    modified.id should equal(original.id)
    modified.email should equal("new@example.com")
    modified.name should equal("New Name")
    modified.updatedAt should equal(2000L)
    modified.createdAt should equal(original.createdAt)
    modified.password should equal(original.password)
  }

  it should "preserve version numbers" in {
    val member = Member(
      id = IDs.ulid(),
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
      versions = 5
    )

    member.versions should equal(5)
  }

  "Member equality" should "compare by value" in {
    val member1 = Member(
      id = "same-id",
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val member2 = Member(
      id = "same-id",
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    member1 should equal(member2)
  }

  it should "differentiate when values differ" in {
    val member1 = Member(
      id = "id-1",
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val member2 = Member(
      id = "id-2",
      googleId = None,
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    member1 should not equal member2
  }

  it should "differentiate when googleId differs" in {
    val member1 = Member(
      id = "same-id",
      googleId = Some("google-id-1"),
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    val member2 = Member(
      id = "same-id",
      googleId = Some("google-id-2"),
      email = "test@example.com",
      password = "password",
      name = "Test User",
      createdAt = 1000L,
      updatedAt = 1000L
    )

    member1 should not equal member2
  }
}
