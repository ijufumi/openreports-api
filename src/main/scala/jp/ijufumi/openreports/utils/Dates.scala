package jp.ijufumi.openreports.utils

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}

object Dates {
  def toLocalDateTime(date: java.util.Date): LocalDateTime = {
    date.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime
  }

  def format(dateTime: LocalDateTime, pattern: String = "yyyyMMddHHMMss"): String = {
    DateTimeFormatter.ofPattern(pattern).format(dateTime)
  }
}
