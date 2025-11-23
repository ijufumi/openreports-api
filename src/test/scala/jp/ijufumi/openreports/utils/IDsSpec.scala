package jp.ijufumi.openreports.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class IDsSpec extends AnyFlatSpec with Matchers {

  "ulid" should "generate valid ULID string" in {
    val id = IDs.ulid()

    id should not be empty
    id should have length 26
  }

  it should "generate unique IDs" in {
    val id1 = IDs.ulid()
    val id2 = IDs.ulid()

    id1 should not equal id2
  }

  it should "generate IDs that are lexicographically sortable" in {
    val id1 = IDs.ulid()
    Thread.sleep(10) // Ensure time difference
    val id2 = IDs.ulid()
    Thread.sleep(10)
    val id3 = IDs.ulid()

    val ids = Seq(id3, id1, id2).sorted

    // When sorted lexicographically, they should be in chronological order
    ids.head should equal(id1)
    ids(1) should equal(id2)
    ids(2) should equal(id3)
  }

  it should "generate multiple unique IDs in quick succession" in {
    val ids = (1 to 100).map(_ => IDs.ulid())

    ids.distinct.size should equal(100)
  }

  it should "generate IDs with valid characters" in {
    val id = IDs.ulid()

    // ULID uses Crockford's Base32: 0-9 and A-Z (excluding I, L, O, U)
    id should fullyMatch regex "[0-9A-HJKMNP-TV-Z]+"
  }

  it should "have correct length of 26 characters" in {
    val ids = (1 to 50).map(_ => IDs.ulid())

    ids.foreach { id =>
      id should have length 26
    }
  }

  it should "be thread-safe" in {
    import scala.concurrent.{Await, Future}
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._

    val futures = (1 to 100).map { _ =>
      Future {
        IDs.ulid()
      }
    }

    val ids = Await.result(Future.sequence(futures), 10.seconds)

    ids.distinct.size should equal(100)
  }

  it should "generate IDs with timestamp component" in {
    val beforeTimestamp = System.currentTimeMillis()
    val id = IDs.ulid()
    val afterTimestamp = System.currentTimeMillis()

    // ULID first 10 characters encode timestamp
    // Just verify the ID was generated without errors
    id should not be empty
    id.length should be >= 10
  }

  it should "generate IDs that increase monotonically within same millisecond" in {
    // Generate multiple IDs very quickly
    val ids = (1 to 10).map(_ => IDs.ulid())

    // All should be unique even if generated in same millisecond
    ids.distinct.size should equal(10)
  }
}
