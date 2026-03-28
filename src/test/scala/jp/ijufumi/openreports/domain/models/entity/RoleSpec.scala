package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RoleSpec extends AnyFlatSpec with Matchers {

  "Role" should "be creatable with valid fields" in {
    val role = Role(
      id = IDs.ulid(),
      roleType = RoleTypes.Admin,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    role should not be null
    role.roleType should equal(RoleTypes.Admin)
  }

  it should "maintain immutability" in {
    val role1 = Role(
      id = "role-id",
      roleType = RoleTypes.Admin,
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val role2 = role1.copy(roleType = RoleTypes.Developer)

    role1.roleType should equal(RoleTypes.Admin)
    role2.roleType should equal(RoleTypes.Developer)
    role1.id should equal(role2.id)
  }

  it should "support Admin role type" in {
    val role = Role(
      id = IDs.ulid(),
      roleType = RoleTypes.Admin,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    role.roleType should equal(RoleTypes.Admin)
  }

  it should "support Developer role type" in {
    val role = Role(
      id = IDs.ulid(),
      roleType = RoleTypes.Developer,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    role.roleType should equal(RoleTypes.Developer)
  }

  it should "support Viewer role type" in {
    val role = Role(
      id = IDs.ulid(),
      roleType = RoleTypes.Viewer,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
    )

    role.roleType should equal(RoleTypes.Viewer)
  }

  it should "preserve timestamps" in {
    val createdAt = System.currentTimeMillis()
    val updatedAt = createdAt + 1000

    val role = Role(
      id = IDs.ulid(),
      roleType = RoleTypes.Admin,
      createdAt = createdAt,
      updatedAt = updatedAt,
    )

    role.createdAt should equal(createdAt)
    role.updatedAt should equal(updatedAt)
    role.updatedAt should be > role.createdAt
  }

  it should "support copy with modifications" in {
    val original = Role(
      id = "role-id",
      roleType = RoleTypes.Admin,
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val modified = original.copy(
      roleType = RoleTypes.Viewer,
      updatedAt = 2000L,
    )

    modified.id should equal(original.id)
    modified.roleType should equal(RoleTypes.Viewer)
    modified.updatedAt should equal(2000L)
    modified.createdAt should equal(original.createdAt)
  }

  it should "preserve version numbers" in {
    val role = Role(
      id = IDs.ulid(),
      roleType = RoleTypes.Admin,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis(),
      versions = 5,
    )

    role.versions should equal(5)
  }

  "Role equality" should "compare by value" in {
    val role1 = Role(
      id = "same-id",
      roleType = RoleTypes.Admin,
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val role2 = Role(
      id = "same-id",
      roleType = RoleTypes.Admin,
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    role1 should equal(role2)
  }

  it should "differentiate when values differ" in {
    val role1 = Role(
      id = "id-1",
      roleType = RoleTypes.Admin,
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val role2 = Role(
      id = "id-2",
      roleType = RoleTypes.Admin,
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    role1 should not equal role2
  }

  it should "differentiate when role type differs" in {
    val role1 = Role(
      id = "same-id",
      roleType = RoleTypes.Admin,
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    val role2 = Role(
      id = "same-id",
      roleType = RoleTypes.Developer,
      createdAt = 1000L,
      updatedAt = 1000L,
    )

    role1 should not equal role2
  }

  "RoleTypes" should "have exactly three role types" in {
    val allRoles = Seq(RoleTypes.Admin, RoleTypes.Developer, RoleTypes.Viewer)

    allRoles should have size 3
  }

  it should "have unique string values" in {
    val roleStrings = Seq(
      RoleTypes.Admin.toString,
      RoleTypes.Developer.toString,
      RoleTypes.Viewer.toString,
    )

    roleStrings.distinct should have size 3
  }
}
