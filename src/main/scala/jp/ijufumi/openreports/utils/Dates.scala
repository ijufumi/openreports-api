package jp.ijufumi.openreports.utils

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneId}
import java.util.TimeZone

object Dates {
  def toLocalDateTime(date: java.util.Date): LocalDateTime = {
    val timezone = TimeZone.getTimeZone("GMT")
    date.toInstant.atZone(timezone.toZoneId).toLocalDateTime
  }

  def format(dateTime: LocalDateTime, pattern: String = "yyyyMMddHHmmssSSS"): String = {
    DateTimeFormatter.ofPattern(pattern).format(dateTime)
  }

  def todayString(pattern: String = "yyyyMMddHHmmssSSS"): String = {
    this.format(LocalDateTime.now(), pattern)
  }

  def currentTimestamp(): Long = {
    Instant.now().toEpochMilli
  }

  def currentTimestampNano(): Long = {
    System.nanoTime()
  }

}
