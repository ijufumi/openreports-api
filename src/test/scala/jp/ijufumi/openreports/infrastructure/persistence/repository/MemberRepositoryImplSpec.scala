package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.models.entity.Member
import jp.ijufumi.openreports.infrastructure.persistence.H2DatabaseHelper
import jp.ijufumi.openreports.utils.IDs
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

class MemberRepositoryImplSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  var db: Database = _
  val repository = new MemberRepositoryImpl()

  override def beforeAll(): Unit = {
    super.beforeAll()
    db = H2DatabaseHelper.createDatabase("member_test")
    H2DatabaseHelper.createSchema(db, memberQuery)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    H2DatabaseHelper.closeDatabase(db)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    H2DatabaseHelper.truncateTables(db, memberQuery)
  }

  "MemberRepositoryImpl" should "be instantiable" in {
    noException should be thrownBy new MemberRepositoryImpl()
  }

  "getById" should "return member when exists" in {
    val memberId = IDs.ulid()

    val member = Member(
      id = memberId,
      googleId = None,
      email = "test@example.com",
      password = "hashed-password",
      name = "Test User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, member)

    val result = repository.getById(db, memberId)

    result should be(defined)
    result.get.id should equal(memberId)
    result.get.email should equal("test@example.com")
    result.get.name should equal("Test User")
  }

  it should "return None when member doesn't exist" in {
    val result = repository.getById(db, "non-existent-id")

    result should be(None)
  }

  "getByGoogleId" should "return member when exists" in {
    val googleId = "google-123456"
    val memberId = IDs.ulid()

    val member = Member(
      id = memberId,
      googleId = Some(googleId),
      email = "test@example.com",
      password = "",
      name = "Google User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, member)

    val result = repository.getByGoogleId(db, googleId)

    result should be(defined)
    result.get.googleId should equal(Some(googleId))
  }

  it should "return None when member with google ID doesn't exist" in {
    val result = repository.getByGoogleId(db, "non-existent-google-id")

    result should be(None)
  }

  "getMemberByEmail" should "return member when exists" in {
    val email = "unique@example.com"
    val memberId = IDs.ulid()

    val member = Member(
      id = memberId,
      googleId = None,
      email = email,
      password = "hashed-password",
      name = "Email User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, member)

    val result = repository.getMemberByEmail(db, email)

    result should be(defined)
    result.get.email should equal(email)
  }

  it should "return None when member with email doesn't exist" in {
    val result = repository.getMemberByEmail(db, "nonexistent@example.com")

    result should be(None)
  }

  "register" should "create new member and return it" in {
    val memberId = IDs.ulid()

    val member = Member(
      id = memberId,
      googleId = None,
      email = "new@example.com",
      password = "hashed-password",
      name = "New User",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    val result = repository.register(db, member)

    result should be(defined)
    result.get.id should equal(member.id)
    result.get.email should equal(member.email)
  }

  "update" should "update existing member" in {
    val memberId = IDs.ulid()

    val member = Member(
      id = memberId,
      googleId = None,
      email = "update@example.com",
      password = "old-password",
      name = "Old Name",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )

    repository.register(db, member)

    val updatedMember = member.copy(
      name = "Updated Name",
      password = "new-password",
      updatedAt = System.currentTimeMillis()
    )

    repository.update(db, updatedMember)

    val result = repository.getById(db, member.id)
    result should be(defined)
    result.get.name should equal("Updated Name")
    result.get.password should equal("new-password")
  }
}
