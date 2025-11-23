package jp.ijufumi.openreports.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.{Instant, LocalDateTime, ZoneId}
import java.util.{Date, TimeZone}

class DatesSpec extends AnyFlatSpec with Matchers {

  "toLocalDateTime" should "convert java.util.Date to LocalDateTime" in {
    val javaDate = new Date()
    val localDateTime = Dates.toLocalDateTime(javaDate)

    localDateTime should not be null
    localDateTime shouldBe a[LocalDateTime]
  }

  it should "preserve the date and time values" in {
    val instant = Instant.parse("2025-11-19T12:30:45.123Z")
    val javaDate = Date.from(instant)

    val localDateTime = Dates.toLocalDateTime(javaDate)

    val expectedLocalDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime

    localDateTime should equal(expectedLocalDateTime)
  }

  it should "handle epoch time (1970-01-01)" in {
    val epochDate = new Date(0)
    val localDateTime = Dates.toLocalDateTime(epochDate)

    localDateTime should not be null
  }

  it should "handle future dates" in {
    // if this code nothing, time zone is changeable by the environment
    val timezone = TimeZone.getTimeZone("GMT")
    TimeZone.setDefault(timezone)
    val futureDate = Date.from(Instant.parse("2030-12-31T23:59:59Z"))
    val localDateTime = Dates.toLocalDateTime(futureDate)

    localDateTime should not be null
    localDateTime.getYear should equal(2030)
  }

  "format" should "format LocalDateTime with default pattern" in {
    val dateTime = LocalDateTime.of(2025, 11, 19, 14, 30, 45)
    val formatted = Dates.format(dateTime)

    formatted should equal("20251119143045000")
  }

  it should "format LocalDateTime with custom pattern" in {
    val dateTime = LocalDateTime.of(2025, 11, 19, 14, 30, 45)
    val formatted = Dates.format(dateTime, "yyyy-MM-dd HH:mm:ss")

    formatted should equal("2025-11-19 14:30:45")
  }

  it should "handle various date patterns" in {
    val dateTime = LocalDateTime.of(2025, 11, 19, 14, 30, 45)

    Dates.format(dateTime, "yyyy/MM/dd") should equal("2025/11/19")
    Dates.format(dateTime, "dd-MM-yyyy") should equal("19-11-2025")
    Dates.format(dateTime, "HH:mm:ss") should equal("14:30:45")
    Dates.format(dateTime, "yyyy-MM-dd'T'HH:mm:ss") should equal("2025-11-19T14:30:45")
  }

  it should "handle single-digit months and days" in {
    val dateTime = LocalDateTime.of(2025, 1, 5, 9, 5, 3)
    val formatted = Dates.format(dateTime, "yyyy-MM-dd HH:mm:ss")

    formatted should equal("2025-01-05 09:05:03")
  }

  it should "handle midnight time" in {
    val dateTime = LocalDateTime.of(2025, 11, 19, 0, 0, 0)
    val formatted = Dates.format(dateTime, "HH:mm:ss")

    formatted should equal("00:00:00")
  }

  it should "handle end of day time" in {
    val dateTime = LocalDateTime.of(2025, 11, 19, 23, 59, 59)
    val formatted = Dates.format(dateTime, "HH:mm:ss")

    formatted should equal("23:59:59")
  }

  "todayString" should "return current date as string" in {
    val result = Dates.todayString()

    result should not be empty
    result should have length 17 // yyyyMMddHHmmssSSS
    result should fullyMatch regex "\\d{17}"
  }

  it should "use default pattern yyyyMMddHHMMss" in {
    val result = Dates.todayString()

    // Should be parseable as a number
    result.toLong should be > 0L
  }

  it should "accept custom pattern" in {
    val result = Dates.todayString("yyyy-MM-dd")

    result should fullyMatch regex "\\d{4}-\\d{2}-\\d{2}"
  }

  it should "generate different values over time" in {
    val time1 = Dates.todayString()
    Thread.sleep(1100) // Sleep for more than 1 second
    val time2 = Dates.todayString()

    time1 should not equal time2
  }

  "currentTimestamp" should "return current epoch seconds" in {
    val beforeTimestamp = Instant.now().toEpochMilli
    val timestamp = Dates.currentTimestamp()
    val afterTimestamp = Instant.now().toEpochMilli

    timestamp should (be >= beforeTimestamp and be <= afterTimestamp)
  }

  it should "return positive number" in {
    val timestamp = Dates.currentTimestamp()

    timestamp should be > 0L
  }

  it should "return values that increase over time" in {
    val timestamp1 = Dates.currentTimestamp()
    Thread.sleep(1100) // Sleep for more than 1 second
    val timestamp2 = Dates.currentTimestamp()

    timestamp2 should be > timestamp1
  }

  it should "return seconds not milliseconds" in {
    val timestamp = Dates.currentTimestamp()
    val currentMillis = System.currentTimeMillis()

    // Epoch seconds should be roughly 1000x smaller than epoch millis
    timestamp should be > (currentMillis / 100)
  }

  "Dates object" should "work consistently across multiple operations" in {
    val javaDate = new Date()
    val localDateTime = Dates.toLocalDateTime(javaDate)
    val formatted = Dates.format(localDateTime)
    val todayStr = Dates.todayString()
    val timestamp = Dates.currentTimestamp()

    formatted should fullyMatch regex "\\d{17}"
    todayStr should fullyMatch regex "\\d{17}"
    timestamp should be > 0L
  }

  it should "handle timezone conversions consistently" in {
    val instant = Instant.now()
    val javaDate = Date.from(instant)

    val localDateTime = Dates.toLocalDateTime(javaDate)
    val expectedLocalDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime

    localDateTime.toLocalDate should equal(expectedLocalDateTime.toLocalDate)
    localDateTime.toLocalTime.getHour should equal(expectedLocalDateTime.toLocalTime.getHour)
  }
}
