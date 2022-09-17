package jp.ijufumi.openreports.utils

import java.time.{LocalDateTime, ZoneId}

object Dates {
  def toLocalDateTime(date: java.util.Date): LocalDateTime = {
    date.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime
  }
}
